package org.gooru.nucleus.handlers.contentmap.bootstrap;

import org.gooru.nucleus.handlers.contentmap.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.contentmap.bootstrap.shutdown.Finalizers;
import org.gooru.nucleus.handlers.contentmap.bootstrap.startup.Initializer;
import org.gooru.nucleus.handlers.contentmap.bootstrap.startup.Initializers;
import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.constants.MessagebusEndpoints;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.MessageDispatcher;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.responses.CombineMessageResponse;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.responses.CombineMessageResponseBuilder;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.responses.HandlerMessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.HandlerDispatchBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 13/2/17.
 */
public class ContentMapVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentMapVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = vertx.eventBus();
        MessageConsumer<JsonObject> consumer = eb.consumer(MessagebusEndpoints.MBEP_CONTENT_MAP);

        consumer.handler(message -> {
            LOGGER.debug("Received message: '{}'", message.body());
            Future<MessageResponse> handlerFuture = appInitializer().compose(result -> {
                return commandExecutor(message);
            });
            Future<HandlerMessageResponse> otherHandlerFuture = handlerCommunicator(message);
            CompositeFuture.<MessageResponse, HandlerMessageResponse> all(handlerFuture, otherHandlerFuture)
                .setHandler(res -> {
                    if (res.succeeded()) {
                        CombineMessageResponse result =
                            CombineMessageResponseBuilder.build(message, (MessageResponse) handlerFuture.result(),
                                (HandlerMessageResponse) otherHandlerFuture.result());
                        message.reply(result.reply(), result.deliveryOptions());
                        JsonObject eventData = result.event();
                        if (eventData != null) {
                            String sessionToken =
                                ((JsonObject) message.body()).getString(MessageConstants.MSG_HEADER_TOKEN);
                            if (sessionToken != null && !sessionToken.isEmpty()) {
                                eventData.put(MessageConstants.MSG_HEADER_TOKEN, sessionToken);
                            } else {
                                LOGGER.warn("Invalid session token received");
                            }
                            eb.send(MessagebusEndpoints.MBEP_EVENT, eventData);
                        }
                    } else {
                        LOGGER.error("Failed to process this command message", res.cause());
                        MessageResponse response = MessageResponseFactory.createInternalErrorResponse();
                        message.reply(response.reply(), response.deliveryOptions());
                    }
                });
        });

        consumer.completionHandler(result -> {
            if (result.succeeded()) {
                LOGGER.info("Content map end point ready to listen");
            } else {
                LOGGER.error("Error registering the content map handler. Halting the content map machinery",
                    result.cause());
                startFuture.fail(result.cause());
                Runtime.getRuntime().halt(1);
            }
        });
    }

    private Future<Void> appInitializer() {
        Future<Void> future = Future.future();
        vertx.<Void> executeBlocking(blockingFuture -> {
            startApplication();
            blockingFuture.complete();
        }, res -> {
            if (res.failed()) {
                future.fail(res.cause());
            }
            future.complete();
        });
        return future;
    }

    private Future<MessageResponse> commandExecutor(Message<JsonObject> message) {
        Future<MessageResponse> future = Future.future();
        vertx.<MessageResponse> executeBlocking(blockingFuture -> {
            MessageResponse result = ProcessorBuilder.build(message).process();
            blockingFuture.complete(result);
        }, res -> {
            if (res.failed()) {
                future.fail(res.cause());
            }
            future.complete(res.result());
        });
        return future;
    }

    private Future<HandlerMessageResponse> handlerCommunicator(Message<JsonObject> message) {
        Future<HandlerMessageResponse> resultFuture = Future.future();
        vertx.<HandlerMessageResponse> executeBlocking(future -> {
            MessageDispatcher dispatcher = HandlerDispatchBuilder.build(message).process();
            if (dispatcher != null) {
                vertx.eventBus().send(dispatcher.address(), dispatcher.message(), dispatcher.options(), reply -> {
                    HandlerMessageResponse result = new HandlerMessageResponse(reply.result());
                    future.complete(result);
                });
            } else {
                future.complete();
            }

        }, false, result -> {
            resultFuture.complete(result.result());
        });
        return resultFuture;
    }

    @Override
    public void stop() throws Exception {
        shutDownApplication();
        super.stop();
    }

    private void startApplication() {
        Initializers initializers = new Initializers();
        try {
            for (Initializer initializer : initializers) {
                initializer.initializeComponent(vertx, config());
            }
        } catch (IllegalStateException ie) {
            LOGGER.error("Error initializing application", ie);
            Runtime.getRuntime().halt(1);
        }
    }

    private void shutDownApplication() {
        Finalizers finalizers = new Finalizers();
        for (Finalizer finalizer : finalizers) {
            finalizer.finalizeComponent();
        }

    }
}

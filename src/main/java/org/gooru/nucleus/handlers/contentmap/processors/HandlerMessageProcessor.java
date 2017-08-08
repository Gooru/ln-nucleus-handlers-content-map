package org.gooru.nucleus.handlers.contentmap.processors;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.HandlerCommandDispatcherBuilder;
import org.gooru.nucleus.handlers.contentmap.handler.communicator.MessageDispatcher;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

class HandlerMessageProcessor implements HandlerProcessor {

    private final Message<JsonObject> message;
    private String userId;
    private JsonObject session;
    private JsonObject request;

    public HandlerMessageProcessor(Message<JsonObject> message) {
        this.message = message;
    }

    @Override
    public MessageDispatcher process() {
        initialize();
        final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
        return HandlerCommandDispatcherBuilder.lookupBuilder(msgOp).build(createContext());
    }

    private ProcessorContext createContext() {
        MultiMap headers = message.headers();
        return new ProcessorContext.ProcessorContextBuilder(userId, session, request, headers).build();
    }

    private void initialize() {
        userId = message.body().getString(MessageConstants.MSG_USER_ID);
        session = message.body().getJsonObject(MessageConstants.MSG_KEY_SESSION);
        request = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
    }
}

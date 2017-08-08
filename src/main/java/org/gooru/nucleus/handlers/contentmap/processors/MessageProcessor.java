package org.gooru.nucleus.handlers.contentmap.processors;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.commands.CommandProcessorBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.MessageResponseWrapperException;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.VersionDeprecatedException;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.contentmap.processors.utils.ValidationHelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

class MessageProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private final Message<JsonObject> message;
    private String userId;
    private JsonObject session;
    private JsonObject request;

    public MessageProcessor(Message<JsonObject> message) {
        this.message = message;
    }

    @Override
    public MessageResponse process() {

        try {
            validateAndInitialize();
            final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
            return CommandProcessorBuilder.lookupBuilder(msgOp).build(createContext()).process();
        } catch (MessageResponseWrapperException mrwe) {
            LOGGER.error("Context validation failed");
            return mrwe.getMessageResponse();
        } catch (VersionDeprecatedException e) {
            LOGGER.error("Version is deprecated");
            return MessageResponseFactory.createVersionDeprecatedResponse();
        } catch (Throwable e) {
            LOGGER.error("Unhandled exception in processing", e);
            return MessageResponseFactory.createInternalErrorResponse(RESOURCE_BUNDLE.getString("unexpected.error"));
        }
    }

    private ProcessorContext createContext() {
        MultiMap headers = message.headers();
        // TODO Check if we need strong typing for any other params or we shall use blanket get from multimap
        return new ProcessorContext.ProcessorContextBuilder(userId, session, request, headers).build();
    }

    private void validateAndInitialize() {
        userId = message.body().getString(MessageConstants.MSG_USER_ID);
        session = message.body().getJsonObject(MessageConstants.MSG_KEY_SESSION);
        request = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);

        validateMessage();
        validateUser();
        validateSession();
        validateRequest();
    }

    private void validateMessage() {
        if (message == null) {
            LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createInvalidRequestResponse(RESOURCE_BUNDLE.getString("invalid.payload")));
        }
    }

    private void validateUser() {
        if (!ValidationHelperUtils.validateUser(userId)) {
            LOGGER.error("Invalid user id passed. Not authorized.");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("invalid.user")));
        }
    }

    private void validateSession() {
        if (session == null || session.isEmpty()) {
            LOGGER.error("Invalid session obtained, probably not authorized properly");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("invalid.session")));
        }
    }

    private void validateRequest() {
        if (request == null) {
            LOGGER.error("Invalid JSON payload on Message Bus");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createInvalidRequestResponse(RESOURCE_BUNDLE.getString("invalid.payload")));
        }
    }

}

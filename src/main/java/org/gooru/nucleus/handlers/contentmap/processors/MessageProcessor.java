package org.gooru.nucleus.handlers.contentmap.processors;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.commands.CommandProcessorBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.VersionDeprecatedException;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
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
            ExecutionResult<MessageResponse> validateResult = validateAndInitialize();
            if (validateResult.isCompleted()) {
                return validateResult.result();
            }
            final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
            return CommandProcessorBuilder.lookupBuilder(msgOp).build(createContext()).process();
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

    private ExecutionResult<MessageResponse> validateAndInitialize() {
        if (message == null) {
            LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(RESOURCE_BUNDLE.getString("invalid.payload")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        userId = message.body().getString(MessageConstants.MSG_USER_ID);
        if (!ValidationHelperUtils.validateUser(userId)) {
            LOGGER.error("Invalid user id passed. Not authorized.");
            return new ExecutionResult<>(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("invalid.user")),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        session = message.body().getJsonObject(MessageConstants.MSG_KEY_SESSION);
        request = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);

        if (session == null || session.isEmpty()) {
            LOGGER.error("Invalid session obtained, probably not authorized properly");
            return new ExecutionResult<>(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("invalid.session")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        if (request == null) {
            LOGGER.error("Invalid JSON payload on Message Bus");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(RESOURCE_BUNDLE.getString("invalid.payload")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

}

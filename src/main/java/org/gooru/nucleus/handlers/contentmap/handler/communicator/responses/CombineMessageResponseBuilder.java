package org.gooru.nucleus.handlers.contentmap.handler.communicator.responses;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public final class CombineMessageResponseBuilder {

    private CombineMessageResponseBuilder() {
        throw new AssertionError();
    }

    public static CombineMessageResponse build(Message<JsonObject> message, MessageResponse messageResponse,
        HandlerMessageResponse handlerMessageResponse) {
        final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
        return MessageResponseBuilder.lookupBuilder(msgOp).build(messageResponse, handlerMessageResponse);

    }

}

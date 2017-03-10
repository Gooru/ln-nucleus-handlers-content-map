package org.gooru.nucleus.handlers.contentmap.handler.communicator;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;

import io.vertx.core.json.JsonObject;

final class HandlerRequestUtility {

    protected static JsonObject getMessage(ProcessorContext context, JsonObject messageBody) {
        JsonObject result = new JsonObject();
        result.put(MessageConstants.MSG_HTTP_BODY, messageBody);
        result.put(MessageConstants.MSG_USER_ID, context.userId());
        result.put(MessageConstants.MSG_KEY_SESSION, context.session());
        return result;
    }

    private HandlerRequestUtility() {
        throw new AssertionError();
    }
}

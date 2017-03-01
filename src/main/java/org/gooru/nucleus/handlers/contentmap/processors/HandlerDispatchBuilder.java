package org.gooru.nucleus.handlers.contentmap.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public final class HandlerDispatchBuilder {

    private HandlerDispatchBuilder() {
        throw new AssertionError();
    }

    public static HandlerProcessor build(Message<JsonObject> message) {
        return new HandlerMessageProcessor(message);
    }

}

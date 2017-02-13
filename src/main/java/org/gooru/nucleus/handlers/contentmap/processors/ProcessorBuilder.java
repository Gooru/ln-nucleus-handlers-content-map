package org.gooru.nucleus.handlers.contentmap.processors;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public final class ProcessorBuilder {

    private ProcessorBuilder() {
        throw new AssertionError();
    }

    public static Processor build(Message<JsonObject> message) {
        return new MessageProcessor(message);
    }
}

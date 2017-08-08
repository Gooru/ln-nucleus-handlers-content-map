package org.gooru.nucleus.handlers.contentmap.handler.communicator;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public interface MessageDispatcher {

    String address();

    DeliveryOptions options();

    JsonObject message();
}

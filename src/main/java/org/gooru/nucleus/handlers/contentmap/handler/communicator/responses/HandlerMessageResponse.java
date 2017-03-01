package org.gooru.nucleus.handlers.contentmap.handler.communicator.responses;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class HandlerMessageResponse {
    private final JsonObject reply;
    private JsonObject httpBody;
    private String status;
    private DeliveryOptions deliveryOptions;

    public HandlerMessageResponse(Message<Object> message) {
        this.reply = (JsonObject) message.body();
        this.status = message.headers().get(MessageConstants.MSG_OP_STATUS);
        this.httpBody = reply.getJsonObject(MessageConstants.MSG_HTTP_BODY);
        this.deliveryOptions = new DeliveryOptions().addHeader(MessageConstants.MSG_OP_STATUS, status);
    }

    public JsonObject result() {
        return this.httpBody.getJsonObject(MessageConstants.MSG_HTTP_RESPONSE);
    }

    public boolean success() {
        if (this.status.equalsIgnoreCase(MessageConstants.MSG_OP_STATUS_SUCCESS)) {
            return true;
        }
        return false;
    }

    public JsonObject reply() {
        return reply;
    }

    public DeliveryOptions deliveryOptions() {
        return deliveryOptions;
    }
}

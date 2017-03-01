package org.gooru.nucleus.handlers.contentmap.handler.communicator.responses;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public class CombineMessageResponse {

    private DeliveryOptions deliveryOptions;
    private JsonObject reply;
    private JsonObject event;

    private CombineMessageResponse(DeliveryOptions deliveryOptions, JsonObject reply, JsonObject event) {
        this.deliveryOptions = deliveryOptions;
        this.reply = reply;
        this.event = event;

    }

    public DeliveryOptions deliveryOptions() {
        return this.deliveryOptions;
    }

    public JsonObject reply() {
        return this.reply;
    }

    public JsonObject event() {
        return this.event;
    }

    public static class Builder {

        private final MessageResponse messageResponse;
        private final HandlerMessageResponse handlerMessageResponse;
        private DeliveryOptions deliveryOptions;
        private JsonObject reply;
        private JsonObject event;
        private String combineKey;

        public Builder(MessageResponse messageResponse, HandlerMessageResponse handlerMessageResponse) {
            this.messageResponse = messageResponse;
            this.handlerMessageResponse = handlerMessageResponse;
        }

        public Builder combineKey(String key) {
            this.combineKey = key;
            return this;
        }

        public CombineMessageResponse build() {
            String status = messageResponse.deliveryOptions().getHeaders().get(MessageConstants.MSG_OP_STATUS);
            if (status.equalsIgnoreCase(MessageConstants.MSG_OP_STATUS_SUCCESS) && handlerMessageResponse != null) {
                if (handlerMessageResponse.success()) {
                    JsonObject reply = messageResponse.reply().copy();
                    reply.getJsonObject(MessageConstants.MSG_HTTP_BODY)
                    .getJsonObject(MessageConstants.MSG_HTTP_RESPONSE).put(combineKey, handlerMessageResponse.result());
                    this.reply = reply;
                    this.deliveryOptions = messageResponse.deliveryOptions();
                } else {
                    this.deliveryOptions = handlerMessageResponse.deliveryOptions();
                    this.reply = handlerMessageResponse.reply();
                }
            } else {
                this.deliveryOptions = messageResponse.deliveryOptions();
                this.reply = messageResponse.reply();
            }
            this.event = messageResponse.event();
            return new CombineMessageResponse(deliveryOptions, reply, event);
        }
    }

}

package org.gooru.nucleus.handlers.contentmap.handler.communicator.responses;

import java.util.HashMap;
import java.util.Map;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

public enum MessageResponseBuilder {

    DEFAULT("default") {
        @Override
        public CombineMessageResponse build(MessageResponse messageResponse,
            HandlerMessageResponse handlerMessageResponse) {

            return new CombineMessageResponse.Builder(messageResponse, handlerMessageResponse).build();
        }

    },
    COURSEMAP_COURSE_GET(MessageConstants.MSG_OP_COURSEMAP_COURSE_GET) {
        @Override
        public CombineMessageResponse build(MessageResponse messageResponse,
            HandlerMessageResponse handlerMessageResponse) {
            return new CombineMessageResponse.Builder(messageResponse, handlerMessageResponse)
                .combineKey(MessageConstants.COURSE_PATH).build();
        }
    },
    COURSEMAP_UNIT_GET(MessageConstants.MSG_OP_COURSEMAP_UNIT_GET) {
        @Override
        public CombineMessageResponse build(MessageResponse messageResponse,
            HandlerMessageResponse handlerMessageResponse) {
            return new CombineMessageResponse.Builder(messageResponse, handlerMessageResponse)
                .combineKey(MessageConstants.UNIT_PATH).build();
        }
    },
    COURSEMAP_LESSON_GET(MessageConstants.MSG_OP_COURSEMAP_LESSON_GET) {
        @Override
        public CombineMessageResponse build(MessageResponse messageResponse,
            HandlerMessageResponse handlerMessageResponse) {
            return new CombineMessageResponse.Builder(messageResponse, handlerMessageResponse)
                .combineKey(MessageConstants.LESSON_PATH).build();
        }
    };

    private String name;

    MessageResponseBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, MessageResponseBuilder> LOOKUP = new HashMap<>();

    static {
        for (MessageResponseBuilder builder : values()) {
            LOOKUP.put(builder.name, builder);
        }
    }

    public static MessageResponseBuilder lookupBuilder(String name) {
        MessageResponseBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract CombineMessageResponse build(MessageResponse messageResponse,
        HandlerMessageResponse handlerMessageResponse);
}

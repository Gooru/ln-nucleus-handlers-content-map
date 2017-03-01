package org.gooru.nucleus.handlers.contentmap.handler.communicator;

import java.util.HashMap;
import java.util.Map;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;

public enum HandlerCommandDispatcherBuilder {

    COURSE_GET(MessageConstants.MSG_OP_COURSEMAP_COURSE_GET) {
        @Override
        public MessageDispatcher build(ProcessorContext context) {
            return new FetchCourseMessageDispatcher(context);
        }
    };

    private String name;

    HandlerCommandDispatcherBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, HandlerCommandDispatcherBuilder> LOOKUP = new HashMap<>();

    static {
        for (HandlerCommandDispatcherBuilder builder : values()) {
            LOOKUP.put(builder.name, builder);
        }
    }

    public static HandlerCommandDispatcherBuilder lookupBuilder(String name) {
        HandlerCommandDispatcherBuilder builder = LOOKUP.get(name);
        return builder;
    }
    public abstract MessageDispatcher build(ProcessorContext context);

}

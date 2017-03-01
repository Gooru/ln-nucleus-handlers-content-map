package org.gooru.nucleus.handlers.contentmap.processors.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.Processor;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 2/1/17.
 */
public enum CommandProcessorBuilder {

    DEFAULT("default") {
        private final Logger LOGGER = LoggerFactory.getLogger(CommandProcessorBuilder.class);
        private final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

        @Override
        public Processor build(ProcessorContext context) {
            return () -> {
                LOGGER.error("Invalid operation type passed in, not able to handle");
                return MessageResponseFactory
                    .createInvalidRequestResponse(RESOURCE_BUNDLE.getString("invalid.operation"));
            };
        }
    },
    BOOKMARK_CREATE(MessageConstants.MSG_OP_BOOKMARK_CREATE) {
        @Override
        public Processor build(ProcessorContext context) {
            return new BookmarkCreateProcessor(context);
        }
    },
    BOOKMARK_DELETE(MessageConstants.MSG_OP_BOOKMARK_DELETE) {
        @Override
        public Processor build(ProcessorContext context) {
            return new BookmarkDeleteProcessor(context);
        }
    },
    BOOKMARK_LIST(MessageConstants.MSG_OP_BOOKMARK_LIST) {
        @Override
        public Processor build(ProcessorContext context) {
            return new BookmarkListProcessor(context);
        }
    },
    COURSEMAP_COURSE_GET(MessageConstants.MSG_OP_COURSEMAP_COURSE_GET) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CourseMapCourseFetchProcessor(context);
        }
    },
    COURSEMAP_UNIT_GET(MessageConstants.MSG_OP_COURSEMAP_UNIT_GET) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CourseMapUnitFetchProcessor(context);
        }
    },
    COURSEMAP_LESSON_GET(MessageConstants.MSG_OP_COURSEMAP_LESSON_GET) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CourseMapLessonFetchProcessor(context);
        }
    },
    PATH_CLASS_CONTENT_CREATE(MessageConstants.MSG_OP_PATH_CLASS_CONTENT_CREATE) {
        @Override
        public Processor build(ProcessorContext context) {
            return new ClassContentPathCreateProcessor(context);
        }
    },
    PATH_COURSE_CREATE(MessageConstants.MSG_OP_PATH_COURSE_CREATE) {
        @Override
        public Processor build(ProcessorContext context) {
            return new CourseMapCourseFetchProcessor(context);
        }
    };

    private String name;

    CommandProcessorBuilder(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static final Map<String, CommandProcessorBuilder> LOOKUP = new HashMap<>();

    static {
        for (CommandProcessorBuilder builder : values()) {
            LOOKUP.put(builder.name, builder);
        }
    }

    public static CommandProcessorBuilder lookupBuilder(String name) {
        CommandProcessorBuilder builder = LOOKUP.get(name);
        if (builder == null) {
            return DEFAULT;
        }
        return builder;
    }

    public abstract Processor build(ProcessorContext context);
}

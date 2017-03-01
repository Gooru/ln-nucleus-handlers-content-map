package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers;

import org.gooru.nucleus.handlers.contentmap.app.components.AppConfiguration;
import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;

import io.vertx.core.json.JsonArray;

/**
 * @author ashish on 15/2/17.
 */
public final class DBHelper {

    private DBHelper() {
        throw new AssertionError();
    }

    public static String bookmarkIdFromContext(ProcessorContext context) {
        return context.requestHeaders().get(MessageConstants.BOOKMARK_ID);
    }

    public static Integer getOffsetFromContext(ProcessorContext context) {
        try {
            String offsetFromRequest = readRequestParam(MessageConstants.REQ_PARAM_OFFSET, context);
            return offsetFromRequest != null ? Integer.valueOf(offsetFromRequest) : 0;
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

    public static Integer getLimitFromContext(ProcessorContext context) {
        try {
            String offsetFromRequest = readRequestParam(MessageConstants.REQ_PARAM_LIMIT, context);
            int offset = offsetFromRequest != null ? Integer.valueOf(offsetFromRequest)
                : AppConfiguration.getInstance().getDefaultLimit();
            if (offset <= AppConfiguration.getInstance().getMaxLimit()) {
                return offset;
            }
            return AppConfiguration.getInstance().getMaxLimit();
        } catch (NumberFormatException nfe) {
            return AppConfiguration.getInstance().getDefaultLimit();
        }
    }

    public static String readRequestParam(String param, ProcessorContext context) {
        JsonArray requestParams = context.request().getJsonArray(param);
        if (requestParams == null || requestParams.isEmpty()) {
            return null;
        }

        String value = requestParams.getString(0);
        return (value != null && !value.isEmpty()) ? value : null;
    }

    public static String courseIdFromContext(ProcessorContext context) {
        return context.requestHeaders().get(MessageConstants.COURSE_ID);
    }

    public static String unitIdFromContext(ProcessorContext context) {
        return context.requestHeaders().get(MessageConstants.UNIT_ID);
    }

    public static String lessonIdFromContext(ProcessorContext context) {
        return context.requestHeaders().get(MessageConstants.LESSON_ID);
    }

}

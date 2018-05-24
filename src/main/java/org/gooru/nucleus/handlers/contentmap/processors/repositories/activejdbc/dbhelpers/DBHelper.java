package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.app.components.AppConfiguration;
import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.MessageResponseWrapperException;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityUserNavigationPaths;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.contentmap.processors.utils.ValidationHelperUtils;

import io.vertx.core.json.JsonArray;

/**
 * @author ashish on 15/2/17.
 */
public final class DBHelper {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

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

    private static String readRequestParam(String param, ProcessorContext context) {
        JsonArray requestParams = context.request().getJsonArray(param);
        if (requestParams == null || requestParams.isEmpty()) {
            return null;
        }

        String value = requestParams.getString(0);
        return (value != null && !value.isEmpty()) ? value : null;
    }

    public static String classIdFromContext(ProcessorContext context) {
        return readRequestParam(MessageConstants.CLASS_ID, context);
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

    public static void validateIdAsUUID(String validationTarget, String bundleKey) {
        if (validationTarget == null || validationTarget.isEmpty()
            || !ValidationHelperUtils.validateId(validationTarget)) {
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString(bundleKey)));
        }
    }

    public static boolean checkContentTypeIsCollection(String contentType) {
        return (contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.ASSESSMENT)
            || contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.COLLECTION));
    }

    public static boolean checkContentTypeIsResource(String contentType) {
        return contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.RESOURCE);
    }

    public static boolean checkIsSignatureAssessment(String suggestedContentType) {
        return (suggestedContentType != null
            && suggestedContentType.equalsIgnoreCase(AJEntityUserNavigationPaths.SIGNATURE_ASSESSMENT));
    }

    public static boolean checkIsSystemSuggestionType(String suggestionType) {
        return (suggestionType != null
            && suggestionType.equalsIgnoreCase(AJEntityUserNavigationPaths.SUGGESTION_TYPE_SYSTEM));
    }

    public static boolean checkIsTeacherSuggestionType(String suggestionType) {
        return (suggestionType != null
            && suggestionType.equalsIgnoreCase(AJEntityUserNavigationPaths.SUGGESTION_TYPE_TEACHER));
    }

}

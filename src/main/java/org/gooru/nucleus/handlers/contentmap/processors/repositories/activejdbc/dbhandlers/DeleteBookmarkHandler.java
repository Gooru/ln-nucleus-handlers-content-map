package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers;

import java.util.Map;
import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.MessageResponseWrapperException;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers.DBHelper;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityBookmark;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.contentmap.processors.utils.ValidationHelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 13/2/17.
 */
class DeleteBookmarkHandler implements DBHandler {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteBookmarkHandler.class);
    private String bookmarkId;
    private AJEntityBookmark entityBookmark;

    public DeleteBookmarkHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        try {
            bookmarkId = DBHelper.bookmarkIdFromContext(context);
            validateBookmarkId();
            validateUserId();
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        } catch (MessageResponseWrapperException mrwe) {
            return new ExecutionResult<>(mrwe.getMessageResponse(), ExecutionResult.ExecutionStatus.FAILED);
        }
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        entityBookmark = AJEntityBookmark.findFirst(AJEntityBookmark.FETCH_UNDELETED_BOOKMARK_QUERY, bookmarkId, false);
        if (entityBookmark == null) {
            LOGGER.warn("Bookmark id '{}' not present in DB or it is deleted", bookmarkId);
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("bookmark.not.found")),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        return AuthorizerBuilder.buildDeleteBookmarkAuthorizer(context).authorize(this.entityBookmark);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        entityBookmark.setDeleted();
        boolean result = entityBookmark.save();
        if (!result) {
            LOGGER.error("Bookmark with id '{}' failed to delete", bookmarkId);
            if (this.entityBookmark.hasErrors()) {
                Map<String, String> map = entityBookmark.errors();
                JsonObject errors = new JsonObject();
                map.forEach(errors::put);
                return new ExecutionResult<>(MessageResponseFactory.createValidationErrorResponse(errors),
                    ExecutionResult.ExecutionStatus.FAILED);
            }
        }
        return new ExecutionResult<>(MessageResponseFactory
            .createNoContentResponse(EventBuilderFactory.getDeleteBookmarkEventBuilder(bookmarkId)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);

    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

    private void validateBookmarkId() {
        if (bookmarkId == null || bookmarkId.isEmpty() || !ValidationHelperUtils.validateId(bookmarkId)) {
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("invalid.bookmark")));
        }
    }

    private void validateUserId() {
        if (context.userId() == null || context.userId().isEmpty() || context.userId()
            .equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
            LOGGER.warn("Anonymous user attempting to delete bookmark");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("not.allowed")));
        }
    }
}

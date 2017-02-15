package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityBookmark;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish on 14/2/17.
 */
public class BookmarkOwnerAuthorizer implements Authorizer<AJEntityBookmark> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookmarkOwnerAuthorizer.class);
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    public BookmarkOwnerAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityBookmark model) {
        String owner = model.getUserId();
        if (owner == null || owner.isEmpty() || !owner.equalsIgnoreCase(context.userId())) {
            LOGGER.warn("User '{}' is not owner of class '{}'", context.userId(), model.getId());
            return new ExecutionResult<>(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("not.allowed")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }
}

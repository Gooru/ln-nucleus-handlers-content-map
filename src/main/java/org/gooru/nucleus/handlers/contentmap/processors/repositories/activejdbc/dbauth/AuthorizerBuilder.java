package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityBookmark;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;

/**
 * Created by ashish on 14/2/17.
 */
public final class AuthorizerBuilder {

    private AuthorizerBuilder() {
        throw new AssertionError();
    }

    public static Authorizer<AJEntityBookmark> buildCreateBookmarkAuthorizer(ProcessorContext context) {
        // As long as session token is valid and user is not anonymous, this is ok
        return model -> new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    public static Authorizer<AJEntityBookmark> buildDeleteBookmarkAuthorizer(ProcessorContext context) {
        return new BookmarkOwnerAuthorizer(context);
    }
}

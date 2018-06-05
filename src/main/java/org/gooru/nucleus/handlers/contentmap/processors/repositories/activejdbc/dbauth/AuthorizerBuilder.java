package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityBookmark;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityClass;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;

import io.vertx.core.json.JsonArray;

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

    public static Authorizer<AJEntityBookmark> buildListBookmarkAuthorizer(ProcessorContext context) {
        return model -> new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }
    
    public static Authorizer<AJEntityCourse> buildTenantAuthorizer(ProcessorContext context) {
        return new TenantAuthorizer(context);
    }

    public static Authorizer<AJEntityCourse> buildTenantCollaboratorAuthorizer(ProcessorContext context,
        JsonArray collaborators) {
        return new TenantCollaboratorAuthorizer(context, collaborators);
    }
    
    public static Authorizer<AJEntityClass> buildClassAuthorizer(ProcessorContext context) {
        return new ClassOwnerOrCollaboratorAuthorizer(context);
    }
}

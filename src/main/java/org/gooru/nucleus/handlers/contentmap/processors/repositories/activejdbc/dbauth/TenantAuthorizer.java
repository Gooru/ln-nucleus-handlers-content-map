package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.libs.tenant.TenantTree;
import org.gooru.nucleus.libs.tenant.TenantTreeBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorization;
import org.gooru.nucleus.libs.tenant.contents.ContentTenantAuthorizationBuilder;
import org.gooru.nucleus.libs.tenant.contents.ContentTreeAttributes;

class TenantAuthorizer implements Authorizer<AJEntityCourse> {
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    public TenantAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityCourse model) {
        TenantTree userTenantTree = TenantTreeBuilder.build(context.tenant(), context.tenantRoot());
        TenantTree contentTenantTree = TenantTreeBuilder.build(model.getString(AJEntityCourse.TENANT),
            model.getString(AJEntityCourse.TENANT_ROOT));

        ContentTenantAuthorization authorization = ContentTenantAuthorizationBuilder.build(contentTenantTree,
            userTenantTree, ContentTreeAttributes.build(model.isCoursePublished()));

        if (authorization.canRead()) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return new ExecutionResult<>(
            MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("not.found")),
            ExecutionResult.ExecutionStatus.FAILED);
    }

}

package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityClass;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;

class ClassOwnerOrCollaboratorAuthorizer implements Authorizer<AJEntityClass> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassOwnerOrCollaboratorAuthorizer.class);
    private final ProcessorContext context;
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    ClassOwnerOrCollaboratorAuthorizer(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> authorize(AJEntityClass model) {
        final String courseId = model.getString(AJEntityClass.COURSE_ID);
        final String classId = model.getString(AJEntityClass.ID);
        if (courseId != null && !courseId.isEmpty()) {
            LOGGER.warn("Authorization request for class '{}' which is having a course '{}'", classId, courseId);
        }
        if (checkOwner(model) || checkCollaborator(model)) {
            return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
        }
        return new ExecutionResult<>(
            MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("not.allowed")),
            ExecutionResult.ExecutionStatus.FAILED);
    }

    private boolean checkOwner(AJEntityClass model) {
        final String creatorId = model.getString(AJEntityClass.CREATOR_ID);
        final String classId = model.getString(AJEntityClass.ID);
        if (creatorId == null || creatorId.isEmpty() || !creatorId.equalsIgnoreCase(context.userId())) {
            LOGGER.warn("User '{}' is not owner of class '{}'", context.userId(), classId);
            return false;
        }
        return true;
    }

    private boolean checkCollaborator(AJEntityClass model) {
        final String collaboratorString = model.getString(AJEntityClass.COLLABORATOR);
        final String classId = model.getString(AJEntityClass.ID);
        if (collaboratorString != null && !collaboratorString.isEmpty()) {
            JsonArray collaborators = new JsonArray(collaboratorString);
            if (collaborators.contains(context.userId())) {
                return true;
            }
        }
        LOGGER.warn("User '{}' is not collaborator of class '{}'", context.userId(), classId);
        return false;
    }
}

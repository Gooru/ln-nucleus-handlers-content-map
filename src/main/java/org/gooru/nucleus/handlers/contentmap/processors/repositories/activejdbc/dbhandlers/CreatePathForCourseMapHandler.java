package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.events.EventBuilderFactory;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.MessageResponseWrapperException;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers.DBHelper;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.*;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entitybuilders.EntityBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.PayloadValidator;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult.ExecutionStatus;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

class CreatePathForCourseMapHandler implements DBHandler {

    private final ProcessorContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePathForCourseMapHandler.class);
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private String ctxCourseId;
    private String ctxUnitId;
    private String ctxLessonId;
    private String ctxCollectionId;
    private String targetCourseId;
    private String targetUnitId;
    private String targetLessonId;
    private String targetCollectionId;
    private String targetResourceId;
    private Long parentPathId;
    private String ctxClassId;
    private String targetContentSubType;
    private AJEntityUserNavigationPaths path;
    private AJEntityUserNavigationPaths parentPath;
    private String targetContentType;

    CreatePathForCourseMapHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        try {
            validateUser();
            validateRequestPayloadNotEmpty();
            validateContextRequestFields();
            ctxCourseId = context.request().getString(AJEntityUserNavigationPaths.CTX_COURSE_ID);
            ctxUnitId = context.request().getString(AJEntityUserNavigationPaths.CTX_UNIT_ID);
            ctxLessonId = context.request().getString(AJEntityUserNavigationPaths.CTX_LESSON_ID);
            ctxCollectionId = context.request().getString(AJEntityUserNavigationPaths.CTX_COLLECTION_ID);
            parentPathId = context.request().getLong(AJEntityUserNavigationPaths.PARENT_PATH_ID);
            targetResourceId = context.request().getString(AJEntityUserNavigationPaths.TARGET_RESOURCE_ID);
            targetCollectionId = context.request().getString(AJEntityUserNavigationPaths.TARGET_COLLECTION_ID);
            targetLessonId = context.request().getString(AJEntityUserNavigationPaths.TARGET_LESSON_ID);
            targetUnitId = context.request().getString(AJEntityUserNavigationPaths.TARGET_UNIT_ID);
            targetCourseId = context.request().getString(AJEntityUserNavigationPaths.TARGET_COURSE_ID);
            ctxClassId = context.request().getString(AJEntityUserNavigationPaths.CTX_CLASS_ID);
            targetContentSubType = context.request().getString(AJEntityUserNavigationPaths.TARGET_CONTENT_SUBTYPE);
            targetContentType = context.request().getString(AJEntityUserNavigationPaths.TARGET_CONTENT_TYPE);
            validateMandatoryFields();
        } catch (MessageResponseWrapperException mrwe) {
            return new ExecutionResult<>(mrwe.getMessageResponse(), ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {

        if (DBHelper.checkSubContentTypeIsPreOrPostTestAssessment(targetContentSubType)) {
            LazyList<AJEntityUserNavigationPaths> userNavigationPaths = AJEntityUserNavigationPaths.findBySQL(
                AJEntityUserNavigationPaths.SELECT_VALIDATE_POST_AND_PRE_TEST_ASSESSMENT_PATH, ctxCourseId, ctxUnitId,
                ctxLessonId, context.userId(), targetCollectionId);
            if (!userNavigationPaths.isEmpty()) {
                LOGGER.warn(
                    "This {} assessment {} is already added to this contextual path of course {}, unit {}, lesson  {}, and user {}",
                    targetContentSubType, targetCollectionId, ctxCourseId, ctxUnitId, ctxLessonId, context.userId());
                return new ExecutionResult<>(MessageResponseFactory.createConflictResponse(
                    RESOURCE_BUNDLE.getString("ctxcul.path.assessment.already.added")), ExecutionStatus.FAILED);
            }

        } else if (DBHelper.checkSubContentTypeIsBenchmarkAssessment(targetContentSubType)) {
            LazyList<AJEntityUserNavigationPaths> userNavigationPaths = AJEntityUserNavigationPaths.findBySQL(
                AJEntityUserNavigationPaths.SELECT_VALIDATE_BENCHMARK_ASSESSMENT_PATH, parentPathId, context.userId(),
                targetCollectionId);
            if (!userNavigationPaths.isEmpty()) {
                LOGGER.warn("This benchmark assessment {} is already added to this parent path of {}, and user {}",
                    targetCollectionId, parentPathId, context.userId());
                return new ExecutionResult<>(MessageResponseFactory.createConflictResponse(
                    RESOURCE_BUNDLE.getString("parent.path.assessment.already.added")), ExecutionStatus.FAILED);
            }
        } else if (DBHelper.checkContentTypeIsResource(targetContentType)) {
            LazyList<AJEntityUserNavigationPaths> userNavigationPaths =
                AJEntityUserNavigationPaths.findBySQL(AJEntityUserNavigationPaths.SELECT_VALIDATE_RESOURCE_PATH,
                    ctxCourseId, ctxUnitId, ctxLessonId, ctxCollectionId, context.userId(), targetResourceId);
            if (!userNavigationPaths.isEmpty()) {
                LOGGER.warn(
                    "This resource {} is already added to this contextual path of course {}, unit {}, lesson  {}, collection, and user {}",
                    targetResourceId, ctxCourseId, ctxUnitId, ctxLessonId, ctxCollectionId, context.userId());
                return new ExecutionResult<>(MessageResponseFactory.createConflictResponse(
                    RESOURCE_BUNDLE.getString("ctxculc.path.resource.already.added")), ExecutionStatus.FAILED);
            }
        }

        if (parentPathId == null) {
            LazyList<AJEntityCourse> courses =
                AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE_TO_VALIDATE, ctxCourseId);
            if (courses.isEmpty()) {
                LOGGER.warn("Context course {} not found to fetch lesson, aborting", ctxCourseId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("course.not.found")),
                    ExecutionStatus.FAILED);
            }

            LazyList<AJEntityUnit> ajEntityUnit =
                AJEntityUnit.findBySQL(AJEntityUnit.SELECT_UNIT_TO_VALIDATE, ctxUnitId, ctxCourseId);
            if (ajEntityUnit.isEmpty()) {
                LOGGER.warn("Context unit {} not found, aborting", ctxUnitId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("unit.not.found")),
                    ExecutionStatus.FAILED);
            }

            LazyList<AJEntityLesson> ajEntityLesson =
                AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON_TO_VALIDATE, ctxLessonId, ctxUnitId, ctxCourseId);
            if (ajEntityLesson.isEmpty()) {
                LOGGER.warn("Context lesson {} not found, aborting", ctxLessonId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("lesson.not.found")),
                    ExecutionStatus.FAILED);
            }

        } else {
            LazyList<AJEntityUserNavigationPaths> userNavigationPaths = AJEntityUserNavigationPaths
                .where(AJEntityUserNavigationPaths.SELECT_USER_NAVIGATION_PATHS, parentPathId);
            if (userNavigationPaths.isEmpty()) {
                LOGGER.warn("Path  {} not found, aborting", parentPathId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("path.not.found")),
                    ExecutionStatus.FAILED);
            }
            parentPath = userNavigationPaths.get(0);
        }
        if (ctxCollectionId != null) {
            LazyList<AJEntityCollection> ajEntityCollection =
                AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION_TO_VALIDATE, ctxCollectionId);
            if (ajEntityCollection.isEmpty()) {
                LOGGER.warn("Context collection {} not found, aborting", ctxCollectionId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("collection.not.found")),
                    ExecutionStatus.FAILED);
            }
        }

        if (targetCourseId != null) {
            LazyList<AJEntityCourse> targetCourses =
                AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE_TO_VALIDATE, targetCourseId);
            if (targetCourses.isEmpty()) {
                LOGGER.warn("Target course {} not found to fetch lesson, aborting", targetCourseId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("course.not.found")),
                    ExecutionStatus.FAILED);
            }

            if (targetUnitId != null) {
                LazyList<AJEntityUnit> targetUnits =
                    AJEntityUnit.findBySQL(AJEntityUnit.SELECT_UNIT_TO_VALIDATE, targetUnitId, targetCourseId);
                if (targetUnits.isEmpty()) {
                    LOGGER.warn("Target unit {} not found to fetch lesson, aborting", targetUnitId);
                    return new ExecutionResult<>(
                        MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("unit.not.found")),
                        ExecutionStatus.FAILED);
                }
            }

            if (targetLessonId != null) {
                LazyList<AJEntityLesson> targetLessons = AJEntityLesson
                    .findBySQL(AJEntityLesson.SELECT_LESSON_TO_VALIDATE, targetLessonId, targetUnitId, targetCourseId);
                if (targetLessons.isEmpty()) {
                    LOGGER.warn("Target lesson {} not found, aborting", targetLessonId);
                    return new ExecutionResult<>(
                        MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("lesson.not.found")),
                        ExecutionStatus.FAILED);
                }
            }

            if (targetCollectionId != null) {
                LazyList<AJEntityCollection> targetCollections =
                    AJEntityCollection.findBySQL(AJEntityCollection.SELECT_CUL_COLLECTION_TO_VALIDATE,
                        targetCollectionId, targetLessonId, targetUnitId, targetCourseId);
                if (targetCollections.isEmpty()) {
                    LOGGER.warn("Target collection {} not found, aborting", targetCollectionId);
                    return new ExecutionResult<>(MessageResponseFactory.createNotFoundResponse(
                        RESOURCE_BUNDLE.getString("collection.not.found")), ExecutionStatus.FAILED);
                }
            }

        } else if (targetCollectionId != null) {
            LazyList<AJEntityCollection> targetCollections =
                AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION_TO_VALIDATE, targetCollectionId);
            if (targetCollections.isEmpty()) {
                LOGGER.warn("Target collection {} not found, aborting", targetCollectionId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("collection.not.found")),
                    ExecutionStatus.FAILED);
            }
        }

        if (targetResourceId != null) {
            LazyList<AJEntityOriginalResource> targetResources = AJEntityOriginalResource
                .findBySQL(AJEntityOriginalResource.SELECT_RESOURCE_TO_VALIDATE, targetResourceId);
            if (targetResources.isEmpty()) {
                LOGGER.warn("Target resource {} not found, aborting", targetResourceId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("resource.not.found")),
                    ExecutionStatus.FAILED);
            }
        }

        if (ctxClassId != null) {
            LazyList<AJEntityClass> ajEntityClass =
                AJEntityClass.findBySQL(AJEntityClass.SELECT_CLASS_TO_VALIDATE, ctxClassId);
            if (ajEntityClass.isEmpty()) {
                LOGGER.warn("Context class {} not found, aborting", ctxClassId);
                return new ExecutionResult<>(
                    MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("class.not.found")),
                    ExecutionStatus.FAILED);
            }
        }

        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        path = new AJEntityUserNavigationPaths();
        if (parentPath != null) {
            path.set(AJEntityUserNavigationPaths.CTX_COURSE_ID, parentPath.getTargetCourseId());
            path.set(AJEntityUserNavigationPaths.CTX_UNIT_ID, parentPath.getTargetUnitId());
            path.set(AJEntityUserNavigationPaths.CTX_LESSON_ID, parentPath.getTargetLessonId());
            path.set(AJEntityUserNavigationPaths.PARENT_PATH_ID, parentPath.getParentPathId());
            path.set(AJEntityUserNavigationPaths.PARENT_PATH_TYPE, AJEntityUserNavigationPaths.ALTERNATE_PATH);
        } else {
            path.set(AJEntityUserNavigationPaths.PARENT_PATH_TYPE, AJEntityUserNavigationPaths.COURSE_PATH);
        }
        new DefaultAJEntityUserNavigationPathsBuilder().build(this.path, context.request(),
            AJEntityUserNavigationPaths.getConverterRegistry());
        path.setUserCtxId(context.userId());
        setCtxCollDefaultValIsNullIfSubTypeIsPreOrPost();
        path.save();
        String pathId = path.getId().toString();
        return new ExecutionResult<>(
            MessageResponseFactory.createCreatedResponse(pathId,
                EventBuilderFactory.getCreateCourseMapPathEventBuilder(pathId)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }

    private void setCtxCollDefaultValIsNullIfSubTypeIsPreOrPost() {
        if (DBHelper.checkSubContentTypeIsPreOrPostTestAssessment(targetContentSubType)) {
            path.set(AJEntityUserNavigationPaths.CTX_COLLECTION_ID, null);
        }
    }

    private void validateUser() {
        if ((context.userId() == null) || context.userId().isEmpty()
            || MessageConstants.MSG_USER_ANONYMOUS.equalsIgnoreCase(context.userId())) {
            LOGGER.warn("Invalid user");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("not.allowed")));
        }
    }

    private void validateContextRequestFields() {
        JsonObject errors = new DefaultPayloadValidator().validatePayload(context.request(),
            AJEntityUserNavigationPaths.createFieldSelector(), AJEntityUserNavigationPaths.getValidatorRegistry());
        if (errors != null && !errors.isEmpty()) {
            LOGGER.warn("Validation errors for request");
            throw new MessageResponseWrapperException(MessageResponseFactory.createValidationErrorResponse(errors));
        }
    }

    private void validateMandatoryFields() {
        if (DBHelper.checkContentTypeIsResource(targetContentType)) {
            if (targetResourceId == null || targetResourceId.isEmpty()) {
                LOGGER.warn("Target resource id is missing.");
                throw new MessageResponseWrapperException(MessageResponseFactory
                    .createInvalidRequestResponse(RESOURCE_BUNDLE.getString("missing.target.resource.id")));
            }
            if (ctxCollectionId == null || ctxCollectionId.isEmpty()) {
                LOGGER.warn("Context of collection id is missing.");
                throw new MessageResponseWrapperException(MessageResponseFactory
                    .createInvalidRequestResponse(RESOURCE_BUNDLE.getString("missing.ctx.collection.id")));
            }
        } else if (DBHelper.checkContentTypeIsCollection(targetContentType)
            && DBHelper.checkSubContentTypeIsBenchmarkAssessment(targetContentSubType)) {
            if (parentPathId == null || parentPathId == 0) {
                LOGGER.warn("Parent path id is missing.");
                throw new MessageResponseWrapperException(MessageResponseFactory
                    .createInvalidRequestResponse(RESOURCE_BUNDLE.getString("missing.parent.path.id")));
            }
        }
    }

    private void validateRequestPayloadNotEmpty() {
        if (context.request() == null || context.request().isEmpty()) {
            LOGGER.warn("Empty payload supplied to create class");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createInvalidRequestResponse(RESOURCE_BUNDLE.getString("empty.payload")));
        }
    }

    private static class DefaultPayloadValidator implements PayloadValidator {
    }

    private static class DefaultAJEntityUserNavigationPathsBuilder
        implements EntityBuilder<AJEntityUserNavigationPaths> {
    }
}

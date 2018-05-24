package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.exceptions.MessageResponseWrapperException;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbauth.AuthorizerBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers.DBHelper;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbutils.DbHelperUtil;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityLesson;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityOriginalResource;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityUserNavigationPaths;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult.ExecutionStatus;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

class FetchCourseMapLessonHandler implements DBHandler {

    private final ProcessorContext context;
    private static final Logger LOGGER = LoggerFactory.getLogger(FetchCourseMapLessonHandler.class);
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
    private String courseId;
    private String unitId;
    private String lessonId;
    private String classId;
    private List<String> collectionIds;
    private List<String> resourceIds;

    FetchCourseMapLessonHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        try {
            courseId = DBHelper.courseIdFromContext(context);
            validateCourseId();
            unitId = DBHelper.unitIdFromContext(context);
            validateUnitId();
            lessonId = DBHelper.lessonIdFromContext(context);
            validateLessonId();
            classId = DBHelper.classIdFromContext(context);
            validateClassId();
            validateUser();
        } catch (MessageResponseWrapperException mrwe) {
            return new ExecutionResult<>(mrwe.getMessageResponse(), ExecutionResult.ExecutionStatus.FAILED);
        }
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);

    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        LazyList<AJEntityCourse> courses = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE_TO_VALIDATE, courseId);
        if (courses.isEmpty()) {
            LOGGER.warn("course {} not found to fetch lesson, aborting", courseId);
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("course.not.found")),
                ExecutionStatus.FAILED);
        }

        LazyList<AJEntityLesson> ajEntityLesson =
            AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON_TO_VALIDATE, lessonId, unitId, courseId);
        if (ajEntityLesson.isEmpty()) {
            LOGGER.warn("Unit: '{}' OR Lesson: '{}' not found, aborting", unitId, lessonId);
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("unit.lesson.not.found")),
                ExecutionStatus.FAILED);
        }

        LOGGER.debug("validateRequest() OK");
        return AuthorizerBuilder.buildTenantAuthorizer(this.context).authorize(courses.get(0));
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        final LazyList<AJEntityUserNavigationPaths> paths = getAlternatePaths();
        final JsonArray navigationPathArray = new JsonArray(JsonFormatterBuilder
            .buildSimpleJsonFormatter(false, AJEntityUserNavigationPaths.RESPONSE_FIELDS).toJson(paths));
        final JsonObject response = new JsonObject();

        if (!navigationPathArray.isEmpty()) {
            collectionIds = new ArrayList<>(navigationPathArray.size());
            resourceIds = new ArrayList<>(navigationPathArray.size());
            initializePathsForCollAndRes(navigationPathArray);
            final JsonObject collResDetailsHolder = new JsonObject();
            fetchCollectionDetails(collResDetailsHolder);
            fetchResourceDetails(collResDetailsHolder);
            navigationPathArray.forEach(result -> {
                parseAndMergeResponse(response, collResDetailsHolder, (JsonObject) result);
            });

        }
        return new ExecutionResult<>(
            MessageResponseFactory.createOkayResponse(new JsonObject().put(MessageConstants.ALTERNATE_PATHS, response)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    private void fetchCollectionDetails(JsonObject detailsResponseHolder) {
        if (!collectionIds.isEmpty()) {
            String collectionArrayString = DbHelperUtil.toPostgresArrayString(collectionIds);
            LazyList<AJEntityCollection> collections =
                AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION, collectionArrayString);
            collections.forEach(content -> {
                JsonObject data = new JsonObject();
                data.put(MessageConstants.TITLE, content.getString(MessageConstants.TITLE));
                data.put(MessageConstants.THUMBNAIL, content.getString(MessageConstants.THUMBNAIL));
                detailsResponseHolder.put(content.getString(MessageConstants.ID), data);
            });

            List<Map> collectionContentCount =
                Base.findAll(AJEntityContent.SELECT_CONTENT_COUNT_BY_COLLECTION, collectionArrayString);
            collectionContentCount.stream().forEach(data -> {
                final String key = ((String) data.get(AJEntityContent.CONTENT_FORMAT)).equalsIgnoreCase(
                    AJEntityContent.QUESTION_FORMAT) ? AJEntityContent.QUESTION_COUNT : AJEntityContent.RESOURCE_COUNT;
                detailsResponseHolder.getJsonObject(data.get(AJEntityContent.COLLECTION_ID).toString()).put(key,
                    data.get(AJEntityContent.CONTENT_COUNT));
            });
            List<Map> oeQuestionCountFromDB =
                Base.findAll(AJEntityContent.SELECT_OE_QUESTION_COUNT, collectionArrayString);
            oeQuestionCountFromDB.stream().forEach(data -> {
                detailsResponseHolder.getJsonObject(data.get(AJEntityContent.COLLECTION_ID).toString())
                    .put(AJEntityContent.OE_QUESTION_COUNT, data.get(AJEntityContent.OE_QUESTION_COUNT));
            });
        }
    }

    private void fetchResourceDetails(JsonObject detailsResponseHolder) {
        if (!resourceIds.isEmpty()) {
            String resourceArrayString = DbHelperUtil.toPostgresArrayString(resourceIds);
            LazyList<AJEntityOriginalResource> resources =
                AJEntityOriginalResource.findBySQL(AJEntityOriginalResource.SELECT_RESOURCE, resourceArrayString);
            resources.forEach(content -> {
                JsonObject data = new JsonObject();
                data.put(MessageConstants.TITLE, content.getString(MessageConstants.TITLE));
                data.put(MessageConstants.THUMBNAIL, content.getString(MessageConstants.THUMBNAIL));
                data.put(AJEntityOriginalResource.CONTENT_SUBFORMAT,
                    content.getString(AJEntityOriginalResource.CONTENT_SUBFORMAT));
                detailsResponseHolder.put(content.getString(MessageConstants.ID), data);
            });
        }
    }

    private void initializePathsForCollAndRes(JsonArray results) {
        results.forEach(content -> {
            final JsonObject suggestedContent = (JsonObject) content;
            if (DBHelper.checkContentTypeIsCollection(
                suggestedContent.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_TYPE))) {
                collectionIds.add(suggestedContent.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_ID));
            } else if (DBHelper.checkContentTypeIsResource(
                suggestedContent.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_TYPE))) {
                resourceIds.add(suggestedContent.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_ID));
            }

        });
    }

    private LazyList<AJEntityUserNavigationPaths> getAlternatePaths() {
        LazyList<AJEntityUserNavigationPaths> paths;
        if (classId != null) {
            paths = AJEntityUserNavigationPaths.findBySQL(
                AJEntityUserNavigationPaths.FETCH_ALTERNATE_PATHS_FOR_USER_IN_CLASS, courseId, unitId, lessonId,
                context.userId(), classId);
        } else {
            paths = AJEntityUserNavigationPaths.findBySQL(AJEntityUserNavigationPaths.FETCH_ALTERNATE_PATHS_FOR_USER,
                courseId, unitId, lessonId, context.userId());

        }
        return paths;
    }

    private void validateCourseId() {
        DBHelper.validateIdAsUUID(courseId, "invalid.course");
    }

    private void validateUnitId() {
        DBHelper.validateIdAsUUID(unitId, "invalid.unit");
    }

    private void validateLessonId() {
        DBHelper.validateIdAsUUID(lessonId, "invalid.lesson");
    }

    private void validateClassId() {
        if (classId == null) {
            return;
        }
        DBHelper.validateIdAsUUID(classId, "invalid.class");
    }

    private void validateUser() {
        if ((context.userId() == null) || context.userId().isEmpty()
            || MessageConstants.MSG_USER_ANONYMOUS.equalsIgnoreCase(context.userId())) {
            LOGGER.warn("Invalid user");
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createForbiddenResponse(RESOURCE_BUNDLE.getString("not.allowed")));
        }
    }

    private static void parseAndMergeResponse(JsonObject response, JsonObject collResDetails, JsonObject path) {
        final String suggestionType = path.getString(AJEntityUserNavigationPaths.SUGGESTION_TYPE);
        final String suggestedContentSubType = path.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_SUBTYPE);
        if (!(DBHelper.checkIsSystemSuggestionType(suggestionType)
            && DBHelper.checkIsSignatureAssessment(suggestedContentSubType))) {
            final String contentId = path.getString(AJEntityUserNavigationPaths.SUGGESTED_CONTENT_ID);
            final JsonObject content = collResDetails.getJsonObject(contentId);
            if (content != null) {
                final JsonObject alternatePath = path.mergeIn(collResDetails.getJsonObject(contentId)).copy();
                alternatePath.remove(AJEntityUserNavigationPaths.SUGGESTION_TYPE);
                if (DBHelper.checkIsSystemSuggestionType(suggestionType)) {
                    final JsonArray systemSuggestions =
                        response.containsKey(AJEntityUserNavigationPaths.SYSTEM_SUGGESTIONS)
                            ? response.getJsonArray(AJEntityUserNavigationPaths.SYSTEM_SUGGESTIONS) : new JsonArray();
                    systemSuggestions.add(alternatePath);
                    response.put(AJEntityUserNavigationPaths.SYSTEM_SUGGESTIONS, systemSuggestions);
                } else if (DBHelper.checkIsTeacherSuggestionType(suggestionType)) {
                    final JsonArray teacherSuggestions =
                        response.containsKey(AJEntityUserNavigationPaths.TEACHER_SUGGESTIONS)
                            ? response.getJsonArray(AJEntityUserNavigationPaths.TEACHER_SUGGESTIONS) : new JsonArray();
                    teacherSuggestions.add(alternatePath);
                    response.put(AJEntityUserNavigationPaths.TEACHER_SUGGESTIONS, teacherSuggestions);
                }
            }
        }

    }

}

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
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityUnit;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities.AJEntityUserNavigationPaths;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult.ExecutionStatus;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.contentmap.processors.utils.ValidationHelperUtils;
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

    public FetchCourseMapLessonHandler(ProcessorContext context) {
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

        LazyList<AJEntityUnit> ajEntityUnit =
            AJEntityUnit.findBySQL(AJEntityUnit.SELECT_UNIT_TO_VALIDATE, unitId, courseId);
        if (ajEntityUnit.isEmpty()) {
            LOGGER.warn("Unit {} not found, aborting", unitId);
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("unit.not.found")),
                ExecutionStatus.FAILED);
        }

        LazyList<AJEntityLesson> ajEntityLesson =
            AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON_TO_VALIDATE, lessonId, unitId, courseId);
        if (ajEntityLesson.isEmpty()) {
            LOGGER.warn("Lesson {} not found, aborting", lessonId);
            return new ExecutionResult<>(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("lesson.not.found")),
                ExecutionStatus.FAILED);
        }

        LOGGER.debug("validateRequest() OK");
        return AuthorizerBuilder.buildTenantAuthorizer(this.context).authorize(courses.get(0));
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        LazyList<AJEntityUserNavigationPaths> paths = AJEntityUserNavigationPaths
            .findBySQL(AJEntityUserNavigationPaths.FETCH_ALTERNATE_PATHS, courseId, unitId, lessonId);

        JsonArray results = new JsonArray(JsonFormatterBuilder
            .buildSimpleJsonFormatter(false, AJEntityUserNavigationPaths.RESPONSE_FIELDS).toJson(paths));

        JsonArray resultSet = new JsonArray();
        if (results.size() > 0) {
            final List<String> lessonIds = new ArrayList<>();
            final List<String> collectionIds = new ArrayList<>();
            results.forEach(content -> {
                JsonObject targetContent = (JsonObject) content;
                if (checkContentTypeIsCollection(
                    targetContent.getString(AJEntityUserNavigationPaths.TARGET_CONTENT_TYPE))) {
                    collectionIds.add(targetContent.getString(AJEntityUserNavigationPaths.TARGET_COLLECTION_ID));
                } else if (checkContentTypeIsLesson(
                    targetContent.getString(AJEntityUserNavigationPaths.TARGET_CONTENT_TYPE))) {
                    lessonIds.add(targetContent.getString(AJEntityUserNavigationPaths.TARGET_LESSON_ID));
                }

            });
            JsonObject targetContentOtherData = new JsonObject();
            if (lessonIds.size() > 0) {
                String lessonArrayString = DbHelperUtil.toPostgresArrayString(lessonIds);
                LazyList<AJEntityLesson> lessons =
                    AJEntityLesson.findBySQL(AJEntityLesson.SELECT_LESSON, lessonArrayString);
                lessons.forEach(content -> {
                    JsonObject data = new JsonObject();
                    data.put(MessageConstants.TITLE, content.getString(MessageConstants.TITLE));
                    targetContentOtherData.put(content.getString(MessageConstants.ID_LESSON), data);
                });
            }

            if (collectionIds.size() > 0) {
                String collectionArrayString = DbHelperUtil.toPostgresArrayString(collectionIds);
                LazyList<AJEntityCollection> collections =
                    AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTION, collectionArrayString);
                collections.forEach(content -> {
                    JsonObject data = new JsonObject();
                    data.put(MessageConstants.TITLE, content.getString(MessageConstants.TITLE));
                    data.put(MessageConstants.THUMBNAIL, content.getString(MessageConstants.THUMBNAIL));
                    targetContentOtherData.put(content.getString(MessageConstants.ID), data);
                });
                List<Map> collectionContentCount =
                    Base.findAll(AJEntityContent.SELECT_CONTENT_COUNT_BY_COLLECTION, collectionArrayString);
                collectionContentCount.stream().forEach(data -> {
                    final String key = ((String) data.get(AJEntityContent.CONTENT_FORMAT))
                        .equalsIgnoreCase(AJEntityContent.QUESTION_FORMAT) ? AJEntityContent.QUESTION_COUNT
                            : AJEntityContent.RESOURCE_COUNT;
                    targetContentOtherData.getJsonObject(data.get(AJEntityContent.COLLECTION_ID).toString()).put(key,
                        data.get(AJEntityContent.CONTENT_COUNT));
                });
                List<Map> oeQuestionCountFromDB =
                    Base.findAll(AJEntityContent.SELECT_OE_QUESTION_COUNT, collectionArrayString);
                oeQuestionCountFromDB.stream().forEach(data -> {
                    targetContentOtherData.getJsonObject(data.get(AJEntityContent.COLLECTION_ID).toString())
                        .put(AJEntityContent.OE_QUESTION_COUNT, data.get(AJEntityContent.OE_QUESTION_COUNT));
                });
            }
            results.forEach(result -> {
                final JsonObject data = ((JsonObject) result);
                final String contentType = data.getString(AJEntityUserNavigationPaths.TARGET_CONTENT_TYPE);
                String contentId = null;
                if (checkContentTypeIsLesson(contentType)) {
                    if (targetContentOtherData
                        .containsKey(data.getString(AJEntityUserNavigationPaths.TARGET_LESSON_ID))) {
                        contentId = data.getString(AJEntityUserNavigationPaths.TARGET_LESSON_ID);
                    }
                } else if (checkContentTypeIsCollection(contentType)) {
                    if (targetContentOtherData
                        .containsKey(data.getString(AJEntityUserNavigationPaths.TARGET_COLLECTION_ID))) {
                        contentId = data.getString(AJEntityUserNavigationPaths.TARGET_COLLECTION_ID);
                    }
                }

                if (contentId != null) {
                    data.mergeIn(targetContentOtherData.getJsonObject(contentId));
                }
                resultSet.add(data);
            });

        }
        return new ExecutionResult<>(
            MessageResponseFactory
                .createOkayResponse(new JsonObject().put(MessageConstants.ALTERNATE_PATHS, resultSet)),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return true;
    }

    private void validateCourseId() {
        if (courseId == null || courseId.isEmpty() || !ValidationHelperUtils.validateId(courseId)) {
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("invalid.course")));
        }
    }

    private void validateUnitId() {
        if (unitId == null || unitId.isEmpty() || !ValidationHelperUtils.validateId(unitId)) {
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("invalid.unit")));
        }
    }

    private void validateLessonId() {
        if (lessonId == null || lessonId.isEmpty() || !ValidationHelperUtils.validateId(lessonId)) {
            throw new MessageResponseWrapperException(
                MessageResponseFactory.createNotFoundResponse(RESOURCE_BUNDLE.getString("invalid.lesson")));
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

    private static boolean checkContentTypeIsCollection(String contentType) {
        return (contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.ASSESSMENT)
            || contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.COLLECTION));
    }

    private static boolean checkContentTypeIsLesson(String contentType) {
        return contentType.equalsIgnoreCase(AJEntityUserNavigationPaths.LESSON);
    }
}

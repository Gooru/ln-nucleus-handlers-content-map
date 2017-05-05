package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import java.util.*;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.converters.ConverterRegistry;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.converters.FieldConverter;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.FieldSelector;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators.ValidatorRegistry;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("user_navigation_paths")
public class AJEntityUserNavigationPaths extends Model {

    private static final String CTX_USER_ID = "ctx_user_id";
    public static final String CTX_COURSE_ID = "ctx_course_id";
    public static final String CTX_UNIT_ID = "ctx_unit_id";
    public static final String CTX_LESSON_ID = "ctx_lesson_id";
    public static final String CTX_COLLECTION_ID = "ctx_collection_id";
    public static final String CTX_CLASS_ID = "ctx_class_id";
    public static final String PARENT_PATH_ID = "parent_path_id";
    public static final String PARENT_PATH_TYPE = "parent_path_type";
    public static final String TARGET_COURSE_ID = "target_course_id";
    public static final String TARGET_UNIT_ID = "target_unit_id";
    public static final String TARGET_LESSON_ID = "target_lesson_id";
    public static final String TARGET_COLLECTION_ID = "target_collection_id";
    public static final String TARGET_RESOURCE_ID = "target_resource_id";
    public static final String TARGET_CONTENT_TYPE = "target_content_type";
    public static final String TARGET_CONTENT_SUBTYPE = "target_content_subtype";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    public static final String PATH_ID = "path_id";
    public static final String LESSON = "lesson";
    public static final String COLLECTION = "collection";
    public static final String ASSESSMENT = "assessment";
    public static final String RESOURCE = "resource";
    private static final String PRE_TEST = "pre-test";
    private static final String POST_TEST = "post-test";
    private static final String BENCHMARK = "benchmark";
    public static final String COURSE_PATH = "course-path";
    public static final String ALTERNATE_PATH = "alternate-path";
    public static final Set<String> CREATABLE_FIELDS = new HashSet<>(
        Arrays.asList(CTX_USER_ID, CTX_COURSE_ID, CTX_UNIT_ID, CTX_LESSON_ID, CTX_COLLECTION_ID, CTX_CLASS_ID,
            PARENT_PATH_ID, PARENT_PATH_TYPE, TARGET_COURSE_ID, TARGET_UNIT_ID, TARGET_LESSON_ID, TARGET_COLLECTION_ID,
            TARGET_CONTENT_TYPE, TARGET_CONTENT_SUBTYPE, TARGET_RESOURCE_ID, CREATED_AT, UPDATED_AT));
    public static final List<String> RESPONSE_FIELDS = Arrays.asList(TARGET_COURSE_ID, TARGET_UNIT_ID, TARGET_LESSON_ID,
        TARGET_COLLECTION_ID, TARGET_CONTENT_TYPE, TARGET_CONTENT_SUBTYPE, MessageConstants.ID, TARGET_RESOURCE_ID,
        CTX_COURSE_ID, CTX_UNIT_ID, CTX_LESSON_ID, CTX_COLLECTION_ID, CTX_CLASS_ID);
    public static final Set<String> MANDATORY_FIELDS = new HashSet<>(Arrays.asList(TARGET_CONTENT_TYPE));
    private static final Set<String> ACCEPT_TARGET_CONTENT_TYPES =
        new HashSet<>(Arrays.asList(LESSON, COLLECTION, ASSESSMENT, RESOURCE));
    private static final Set<String> ACCEPT_TARGET_CONTENT_SUBTYPES =
        new HashSet<>(Arrays.asList(PRE_TEST, POST_TEST, BENCHMARK));
    public static final String SELECT_USER_NAVIGATION_PATHS = "id =  ?::bigint";
    private static final Map<String, FieldValidator> validatorRegistry;
    private static final Map<String, FieldConverter> converterRegistry;
    public static final String FETCH_ALTERNATE_PATHS_FOR_USER =
        "SELECT  id, target_course_id, target_unit_id, target_lesson_id, target_collection_id, target_content_type, "
            + "target_content_subtype, target_resource_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id "
            + ", ctx_class_id from user_navigation_paths where ctx_course_id = ?::uuid AND ctx_unit_id = "
            + "?::uuid AND ctx_lesson_id = ?::uuid AND ctx_user_id = ?::uuid and ctx_class_id is null";
    public static final String FETCH_ALTERNATE_PATHS_FOR_USER_IN_CLASS =
        "SELECT  id, target_course_id, target_unit_id, target_lesson_id, target_collection_id, target_content_type, "
            + "target_content_subtype, target_resource_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id "
            + ", ctx_class_id from user_navigation_paths where ctx_course_id = ?::uuid AND ctx_unit_id = "
            + "?::uuid AND ctx_lesson_id = ?::uuid AND ctx_user_id = ?::uuid and ctx_class_id = ?::uuid";

    static {
        validatorRegistry = initializeValidators();
        converterRegistry = initializeConverters();
    }

    private static Map<String, FieldConverter> initializeConverters() {
        Map<String, FieldConverter> converterMap = new HashMap<>();
        converterMap.put(CTX_COURSE_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(CTX_UNIT_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(CTX_LESSON_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(CTX_COLLECTION_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(CTX_CLASS_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(TARGET_COURSE_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(TARGET_UNIT_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(TARGET_LESSON_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(TARGET_COLLECTION_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(TARGET_RESOURCE_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        return Collections.unmodifiableMap(converterMap);
    }

    private static Map<String, FieldValidator> initializeValidators() {
        Map<String, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(CTX_USER_ID, (FieldValidator::validateUuid));
        validatorMap.put(CTX_COURSE_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(CTX_UNIT_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(CTX_LESSON_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(CTX_CLASS_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_CONTENT_TYPE, (value -> FieldValidator.validateString(value, 128)));
        validatorMap.put(TARGET_CONTENT_SUBTYPE, (value -> FieldValidator.validateString(value, 128)));
        validatorMap.put(PARENT_PATH_TYPE, (value -> FieldValidator.validateString(value, 256)));
        validatorMap.put(CTX_COLLECTION_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_COURSE_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_UNIT_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_LESSON_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_COLLECTION_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_RESOURCE_ID, (value -> FieldValidator.validateUuidIfPresent((String) value)));
        validatorMap.put(TARGET_CONTENT_TYPE,
            (value -> FieldValidator.validateAcceptedValuesIfPresent((String) value, ACCEPT_TARGET_CONTENT_TYPES)));
        validatorMap.put(TARGET_CONTENT_SUBTYPE,
            (value -> FieldValidator.validateAcceptedValuesIfPresent((String) value, ACCEPT_TARGET_CONTENT_SUBTYPES)));
        return Collections.unmodifiableMap(validatorMap);
    }

    public static FieldSelector createFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(CREATABLE_FIELDS);
            }

            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(MANDATORY_FIELDS);
            }
        };
    }

    public Object getTargetCourseId() {
        return this.get(TARGET_COURSE_ID);
    }

    public Object getTargetUnitId() {
        return this.get(TARGET_UNIT_ID);
    }

    public Object getTargetLessonId() {
        return this.get(TARGET_LESSON_ID);
    }

    public Object getTargetCollectionId() {
        return this.get(TARGET_COLLECTION_ID);
    }

    public Object getCtxClassId() {
        return this.get(CTX_CLASS_ID);
    }

    public Long getParentPathId() {
        return this.getLong(PARENT_PATH_ID);
    }

    public String getParentPathType() {
        return this.getString(PARENT_PATH_TYPE);
    }

    public void setUserCtxId(String userCtxId) {
        this.set(CTX_USER_ID, FieldConverter.convertFieldToUuid(userCtxId));
    }

    public static ValidatorRegistry getValidatorRegistry() {
        return new UserNavigationPathsValidationRegistry();
    }

    public static ConverterRegistry getConverterRegistry() {
        return new UserNavigationPathsConverterRegistry();
    }

    private static class UserNavigationPathsValidationRegistry implements ValidatorRegistry {
        @Override
        public FieldValidator lookupValidator(String fieldName) {
            return validatorRegistry.get(fieldName);
        }
    }

    private static class UserNavigationPathsConverterRegistry implements ConverterRegistry {
        @Override
        public FieldConverter lookupConverter(String fieldName) {
            return converterRegistry.get(fieldName);
        }
    }

}

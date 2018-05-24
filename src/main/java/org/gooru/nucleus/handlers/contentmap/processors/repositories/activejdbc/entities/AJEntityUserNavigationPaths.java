package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("user_navigation_paths")
public class AJEntityUserNavigationPaths extends Model {

    public static final String CTX_COURSE_ID = "ctx_course_id";
    public static final String CTX_UNIT_ID = "ctx_unit_id";
    public static final String CTX_LESSON_ID = "ctx_lesson_id";
    public static final String CTX_COLLECTION_ID = "ctx_collection_id";
    public static final String CTX_CLASS_ID = "ctx_class_id";
    public static final String COURSE_PATH = "course-path";
    public static final String ALTERNATE_PATH = "alternate-path";
    public static final String ASSESSMENT = "assessment";
    public static final String COLLECTION = "collection";
    public static final String RESOURCE = "resource";
    public static final String SUGGESTED_CONTENT_ID = "suggested_content_id";
    public static final String SUGGESTION_TYPE = "suggestion_type";
    public static final String SUGGESTED_CONTENT_TYPE = "suggested_content_type";
    public static final String SUGGESTED_CONTENT_SUBTYPE = "suggested_content_subtype";
    public static final String SIGNATURE_ASSESSMENT = "signature-assessment";
    public static final String SUGGESTION_TYPE_SYSTEM = "system";
    public static final String SUGGESTION_TYPE_TEACHER = "teacher";
    public static final String SYSTEM_SUGGESTIONS = "system_suggestions";
    public static final String TEACHER_SUGGESTIONS = "teacher_suggestions";

    public static final List<String> RESPONSE_FIELDS =
        Arrays.asList(SUGGESTED_CONTENT_ID, SUGGESTION_TYPE, SUGGESTED_CONTENT_TYPE, SUGGESTED_CONTENT_SUBTYPE,
            MessageConstants.ID, CTX_COURSE_ID, CTX_UNIT_ID, CTX_LESSON_ID, CTX_COLLECTION_ID, CTX_CLASS_ID);

    public static final String FETCH_ALTERNATE_PATHS_FOR_USER =
        "SELECT  id, suggested_content_id, suggestion_type, suggested_content_type, suggested_content_subtype, ctx_course_id, "
            + "ctx_unit_id, ctx_lesson_id, ctx_collection_id, ctx_class_id from user_navigation_paths where ctx_course_id = ?::uuid "
            + "AND ctx_unit_id = ?::uuid AND ctx_lesson_id = ?::uuid AND ctx_user_id = ?::uuid and ctx_class_id is null ORDER BY id asc";

    public static final String FETCH_ALTERNATE_PATHS_FOR_USER_IN_CLASS =
        "SELECT  id, suggested_content_id, suggestion_type, suggested_content_type, suggested_content_subtype, ctx_course_id, "
            + "ctx_unit_id, ctx_lesson_id, ctx_collection_id, ctx_class_id from user_navigation_paths where ctx_course_id = ?::uuid "
            + "AND ctx_unit_id = ?::uuid AND ctx_lesson_id = ?::uuid AND ctx_user_id = ?::uuid and ctx_class_id = ?::uuid ORDER BY id asc";

}

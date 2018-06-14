package org.gooru.nucleus.handlers.contentmap.constants;

/**
 * @author ashish on 13/2/17.
 */
public final class MessageConstants {

    public static final String MSG_HEADER_OP = "mb.operation";
    public static final String MSG_HEADER_TOKEN = "session.token";
    public static final String MSG_OP_STATUS = "mb.operation.status";
    public static final String MSG_KEY_SESSION = "session";
    public static final String MSG_OP_STATUS_SUCCESS = "success";
    public static final String MSG_OP_STATUS_ERROR = "error";
    public static final String MSG_OP_STATUS_VALIDATION_ERROR = "error.validation";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_RESPONSE = "http.response";
    public static final String MSG_HTTP_ERROR = "http.error";
    public static final String MSG_HTTP_VALIDATION_ERROR = "http.validation.error";
    public static final String MSG_HTTP_HEADERS = "http.headers";
    public static final String MSG_MESSAGE = "message";
    public static final String MSG_API_VERSION = "api.version";

    // Containers for different responses
    public static final String RESP_CONTAINER_MBUS = "mb.container";
    public static final String RESP_CONTAINER_EVENT = "mb.event";

    // Operations
    public static final String MSG_OP_BOOKMARK_CREATE = "bookmark.create";
    public static final String MSG_OP_BOOKMARK_DELETE = "bookmark.delete";
    public static final String MSG_OP_BOOKMARK_LIST = "bookmark.list";
    public static final String MSG_OP_COURSEMAP_COURSE_GET = "coursemap.course.get";
    public static final String MSG_OP_COURSEMAP_UNIT_GET = "coursemap.unit.get";
    public static final String MSG_OP_COURSEMAP_LESSON_GET = "coursemap.lesson.get";
    public static final String MSG_OP_COURSE_GET = "course.get";
    public static final String MSG_OP_UNIT_GET = "unit.get";
    public static final String MSG_OP_LESSON_GET = "lesson.get";

    // mics
    public static final String BOOKMARK_ID = "bookmarkId";
    public static final String REQ_PARAM_OFFSET = "offset";
    public static final String REQ_PARAM_LIMIT = "limit";
    public static final String BOOKMARKS = "bookmarks";
    public static final String ALTERNATE_PATHS = "alternate_paths";
    public static final String COURSE_PATH = "course_path";
    public static final String COURSE_ID = "courseId";
    public static final String UNIT_ID = "unitId";
    public static final String LESSON_ID = "lessonId";
    public static final String CLASS_ID = "classId";
    public static final String ID_LESSON = "lesson_id";
    public static final String TITLE = "title";
    public static final String THUMBNAIL = "thumbnail";
    public static final String ID = "id";
    public static final String USER_ID = "userId";

    private MessageConstants() {
        throw new AssertionError();
    }

}

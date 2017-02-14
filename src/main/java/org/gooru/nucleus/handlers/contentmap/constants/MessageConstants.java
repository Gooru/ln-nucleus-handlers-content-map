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

    // Containers for different responses
    public static final String RESP_CONTAINER_MBUS = "mb.container";
    public static final String RESP_CONTAINER_EVENT = "mb.event";

    // Operations
    public static final String MSG_OP_BOOKMARK_CREATE = "bookmark.create";
    public static final String MSG_OP_BOOKMARK_DELETE = "bookmark.delete";
    public static final String MSG_OP_BOOKMARK_LIST = "bookmark.list";

    private MessageConstants() {
        throw new AssertionError();
    }

}

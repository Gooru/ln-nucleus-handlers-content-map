package org.gooru.nucleus.handlers.contentmap.processors.responses;

import org.gooru.nucleus.handlers.contentmap.constants.HttpConstants;
import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.events.EventBuilder;

import io.vertx.core.json.JsonObject;

/**
 * Created by ashish on 13/2/17.
 */
public final class MessageResponseFactory {

    private static final String API_VERSION_DEPRECATED = "API version is deprecated";
    private static final String API_VERSION_NOT_SUPPORTED = "API version is not supported";

    private MessageResponseFactory() {
        throw new AssertionError();
    }

    public static MessageResponse createInvalidRequestResponse() {
        return new MessageResponse.Builder().failed().setStatusBadRequest().build();
    }

    public static MessageResponse createForbiddenResponse() {
        return new MessageResponse.Builder().failed().setStatusForbidden().build();
    }

    public static MessageResponse createInternalErrorResponse() {
        return new MessageResponse.Builder().failed().setStatusInternalError().build();
    }

    public static MessageResponse createInvalidRequestResponse(String message) {
        return new MessageResponse.Builder().failed().setStatusBadRequest()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, message)).build();
    }

    public static MessageResponse createForbiddenResponse(String message) {
        return new MessageResponse.Builder().failed().setStatusForbidden()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, message)).build();
    }

    public static MessageResponse createInternalErrorResponse(String message) {
        return new MessageResponse.Builder().failed().setStatusInternalError()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, message)).build();
    }

    public static MessageResponse createNotFoundResponse(String message) {
        return new MessageResponse.Builder().failed().setStatusNotFound()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, message)).build();
    }

    public static MessageResponse createValidationErrorResponse(JsonObject errors) {
        return new MessageResponse.Builder().validationFailed().setStatusBadRequest().setResponseBody(errors).build();

    }

    public static MessageResponse createCreatedResponse(String location, EventBuilder eventBuilder) {
        return new MessageResponse.Builder().successful().setStatusCreated()
            .setHeader(HttpConstants.HEADER_LOCATION, location).setEventData(eventBuilder.build()).build();
    }

    public static MessageResponse createNoContentResponse() {
        return new MessageResponse.Builder().successful().setStatusNoOutput().setResponseBody(new JsonObject()).build();
    }

    public static MessageResponse createNoContentResponse(EventBuilder eventBuilder) {
        return new MessageResponse.Builder().successful().setStatusNoOutput().setResponseBody(new JsonObject())
            .setEventData(eventBuilder.build()).build();
    }

    public static MessageResponse createOkayResponse(JsonObject body) {
        return new MessageResponse.Builder().successful().setStatusOkay().setResponseBody(body).build();
    }

    public static MessageResponse createVersionDeprecatedResponse() {
        return new MessageResponse.Builder().failed().setStatusHttpCode(HttpConstants.HttpStatus.GONE)
            .setContentTypeJson()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, API_VERSION_DEPRECATED)).build();
    }

    public static MessageResponse createConflictResponse(String message) {
        return new MessageResponse.Builder().failed().setStatusConflict()
            .setResponseBody(new JsonObject().put(MessageConstants.MSG_MESSAGE, message)).build();
    }

    public static MessageResponse createCreatedResponseWithoutEvent(String location) {
        return new MessageResponse.Builder().successful().setStatusCreated()
            .setHeader(HttpConstants.HEADER_LOCATION, location).build();
    }
}

package org.gooru.nucleus.handlers.contentmap.processors.events;

import io.vertx.core.json.JsonObject;

/**
 * Created by ashish on 13/2/17.
 */
public final class EventBuilderFactory {

    private static final String EVT_BOOKMARK_CREATE = "event.bookmark.create";
    private static final String EVT_BOOKMARK_DELETE = "event.bookmark.delete";
    private static final String EVENT_NAME = "event.name";
    private static final String EVENT_BODY = "event.body";
    private static final String BOOKMARK_ID = "id";

    private EventBuilderFactory() {
        throw new AssertionError();
    }

    public static EventBuilder getDeleteBookmarkEventBuilder(String bookmarkId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_BOOKMARK_DELETE)
            .put(EVENT_BODY, new JsonObject().put(BOOKMARK_ID, bookmarkId));
    }

    public static EventBuilder getCreateBookmarkEventBuilder(String bookmarkId) {
        return () -> new JsonObject().put(EVENT_NAME, EVT_BOOKMARK_CREATE)
            .put(EVENT_BODY, new JsonObject().put(BOOKMARK_ID, bookmarkId));
    }

}

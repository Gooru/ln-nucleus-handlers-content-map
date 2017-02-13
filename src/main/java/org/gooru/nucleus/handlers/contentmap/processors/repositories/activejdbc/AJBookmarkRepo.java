package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.BookmarkRepo;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
class AJBookmarkRepo implements BookmarkRepo {

    private final ProcessorContext context;

    AJBookmarkRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse createBookmark() {
        return null;
    }

    @Override
    public MessageResponse deleteBookmark() {
        return null;
    }

    @Override
    public MessageResponse listBookmark() {
        return null;
    }

    @Override
    public MessageResponse getBookmark() {
        return null;
    }
}

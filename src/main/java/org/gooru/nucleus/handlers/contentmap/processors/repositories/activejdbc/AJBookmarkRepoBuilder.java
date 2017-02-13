package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.BookmarkRepo;

/**
 * @author ashish on 13/2/17.
 */
public final class AJBookmarkRepoBuilder {

    private AJBookmarkRepoBuilder() {
        throw new AssertionError();
    }

    public static BookmarkRepo buildBookmarkRepo(ProcessorContext context) {
        return new AJBookmarkRepo(context);
    }
}

package org.gooru.nucleus.handlers.contentmap.processors.commands;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
class BookmarkCreateProcessor extends AbstractCommandProcessor {
    public BookmarkCreateProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        // return RepoBuilder.buildBookmarkRepo(context).createBookmarkHandler();
        throw new AssertionError("Not implemented");
    }

}

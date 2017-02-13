package org.gooru.nucleus.handlers.contentmap.processors.commands;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
class BookmarkGetProcessor extends AbstractCommandProcessor {
    public BookmarkGetProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        // return RepoBuilder.buildBookmarkRepo(context).getBookmarkHandler();
        throw new AssertionError("Not implemented");
    }

}

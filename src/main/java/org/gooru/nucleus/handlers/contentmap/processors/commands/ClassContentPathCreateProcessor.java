package org.gooru.nucleus.handlers.contentmap.processors.commands;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

class ClassContentPathCreateProcessor extends AbstractCommandProcessor {
    public ClassContentPathCreateProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildPathRepo(context).createPathForClassContent();
    }

}

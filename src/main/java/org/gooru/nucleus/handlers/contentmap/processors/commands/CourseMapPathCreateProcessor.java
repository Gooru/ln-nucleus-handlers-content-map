package org.gooru.nucleus.handlers.contentmap.processors.commands;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

class CourseMapPathCreateProcessor extends AbstractCommandProcessor {
    public CourseMapPathCreateProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {

    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildCourseMapRepo(context).createPathForCourseMap();
    }

}

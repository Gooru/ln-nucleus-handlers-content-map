package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.CourseMapRepo;


public final class AJCourseMapRepoBuilder {

    private AJCourseMapRepoBuilder() {
        throw new AssertionError();
    }

    public static CourseMapRepo buildCourseMapRepo(ProcessorContext context) {
        return new AJCourseMapRepo(context);
    }
}

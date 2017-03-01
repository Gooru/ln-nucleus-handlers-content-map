package org.gooru.nucleus.handlers.contentmap.processors.repositories;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.AJBookmarkRepoBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.AJCourseMapRepoBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.AJPathRepoBuilder;

/**
 * @author ashish on 13/2/17.
 */
public final class RepoBuilder {

    private RepoBuilder() {
        throw new AssertionError();
    }

    public static BookmarkRepo buildBookmarkRepo(ProcessorContext context) {
        return AJBookmarkRepoBuilder.buildBookmarkRepo(context);
    }

    public static CourseMapRepo buildCourseMapRepo(ProcessorContext context) {
        return AJCourseMapRepoBuilder.buildCourseMapRepo(context);
    }

    public static PathRepo buildPathRepo(ProcessorContext context) {
        return AJPathRepoBuilder.buildPathRepo(context);
    }
}

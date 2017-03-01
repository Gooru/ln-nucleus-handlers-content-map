package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;

/**
 * Created by ashish on 13/2/17.
 */
public final class DBHandlerBuilder {

    private DBHandlerBuilder() {
        throw new AssertionError();
    }

    public static DBHandler buildCreateBookmarkHandler(ProcessorContext context) {
        return new CreateBookmarkHandler(context);
    }

    public static DBHandler buildListBookmarkHandler(ProcessorContext context) {
        return new ListBookmarkHandler(context);
    }

    public static DBHandler buildDeleteBookmarkHandler(ProcessorContext context) {
        return new DeleteBookmarkHandler(context);
    }

    public static DBHandler buildFetchCourseMapCourseHandler(ProcessorContext context) {
        return new FetchCourseMapCourseHandler(context);
    }

    public static DBHandler buildFetchCourseMapUnitHandler(ProcessorContext context) {
        return new FetchCourseMapUnitHandler(context);
    }

    public static DBHandler buildFetchCourseMapLessonHandler(ProcessorContext context) {
        return new FetchCourseMapLessonHandler(context);
    }

    public static DBHandler buildCreatePathForCourseHandler(ProcessorContext context) {
        return new CreatePathForCourseHandler(context);
    }

    public static DBHandler buildCreatePathForClassContentHandler(ProcessorContext context) {
        return new CreatePathForClassContentHandler(context);
    }
}

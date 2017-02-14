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

}

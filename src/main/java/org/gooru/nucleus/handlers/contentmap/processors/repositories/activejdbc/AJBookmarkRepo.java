package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.BookmarkRepo;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.transactions.TransactionExecutor;
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
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCreateBookmarkHandler(context));
    }

    @Override
    public MessageResponse deleteBookmark() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildDeleteBookmarkHandler(context));
    }

    @Override
    public MessageResponse listBookmark() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildListBookmarkHandler(context));
    }

    @Override
    public MessageResponse getBookmark() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildGetBookmarkHandler(context));
    }
}

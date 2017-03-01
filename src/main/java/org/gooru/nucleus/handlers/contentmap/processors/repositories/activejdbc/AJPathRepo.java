package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.PathRepo;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

class AJPathRepo implements PathRepo {

    private final ProcessorContext context;

    AJPathRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse createPathForCourse() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCreatePathForCourseHandler(context));
    }

    @Override
    public MessageResponse createPathForClassContent() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildCreatePathForClassContentHandler(context));
    }
}

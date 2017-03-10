package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.CourseMapRepo;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;


class AJCourseMapRepo implements CourseMapRepo {

    private final ProcessorContext context;

    AJCourseMapRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse fetchCourseMapCourse() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildFetchCourseMapCourseHandler(context));
    }

    @Override
    public MessageResponse fetchCourseMapUnit() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildFetchCourseMapUnitHandler(context));
    }

    @Override
    public MessageResponse fetchCourseMapLesson() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildFetchCourseMapLessonHandler(context));
    }
}

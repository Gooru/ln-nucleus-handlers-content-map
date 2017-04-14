package org.gooru.nucleus.handlers.contentmap.handler.communicator;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.constants.MessagebusEndpoints;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers.DBHelper;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

public class FetchUnitMessageDispatcher implements MessageDispatcher {

    private final ProcessorContext context;
    private static final String API_VERSION = "v1";
    private final DeliveryOptions options;

    public FetchUnitMessageDispatcher(ProcessorContext context) {
        this.context = context;
        this.options = new DeliveryOptions();
    }

    @Override
    public String address() {
        return MessagebusEndpoints.MBEP_COURSE;
    }

    @Override
    public DeliveryOptions options() {
        options.addHeader(MessageConstants.MSG_HEADER_OP, MessageConstants.MSG_OP_UNIT_GET);
        options.addHeader(MessageConstants.MSG_API_VERSION, API_VERSION);
        options.addHeader(MessageConstants.COURSE_ID, DBHelper.courseIdFromContext(context));
        options.addHeader(MessageConstants.UNIT_ID, DBHelper.unitIdFromContext(context));
        return options;
    }

    @Override
    public JsonObject message() {
        return HandlerRequestUtility.getMessage(context, new JsonObject());
    }
}

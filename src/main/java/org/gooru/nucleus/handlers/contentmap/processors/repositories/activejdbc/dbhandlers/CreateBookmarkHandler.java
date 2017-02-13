package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.gooru.nucleus.handlers.contentmap.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
class CreateBookmarkHandler implements DBHandler {
    public CreateBookmarkHandler(ProcessorContext context) {
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        return null;
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        return null;
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        return null;
    }

    @Override
    public boolean handlerReadOnly() {
        return false;
    }
}

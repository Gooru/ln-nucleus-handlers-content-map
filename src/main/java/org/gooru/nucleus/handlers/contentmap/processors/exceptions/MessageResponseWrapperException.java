package org.gooru.nucleus.handlers.contentmap.processors.exceptions;

import org.gooru.nucleus.handlers.contentmap.processors.responses.MessageResponse;

/**
 * @author ashish on 13/2/17.
 */
public class MessageResponseWrapperException extends RuntimeException {

    private final MessageResponse messageResponse;

    public MessageResponseWrapperException(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }
}

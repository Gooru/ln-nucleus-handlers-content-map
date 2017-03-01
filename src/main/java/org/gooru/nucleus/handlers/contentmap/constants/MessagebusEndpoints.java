package org.gooru.nucleus.handlers.contentmap.constants;

/**
 * @author ashish on 13/2/17.
 */
public final class MessagebusEndpoints {
    /*
     * Any change here in end points should be done in the gateway side as well,
     * as both sender and receiver should be in sync
     */
    public static final String MBEP_CONTENT_MAP = "org.gooru.nucleus.message.bus.content.map";
    public static final String MBEP_EVENT = "org.gooru.nucleus.message.bus.publisher.event";
    public static final String MBEP_COURSE = "org.gooru.nucleus.message.bus.course";

    private MessagebusEndpoints() {
        throw new AssertionError();
    }

}

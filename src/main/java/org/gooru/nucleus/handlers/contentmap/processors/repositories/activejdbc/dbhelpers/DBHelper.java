package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.dbhelpers;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;

/**
 * @author ashish on 15/2/17.
 */
public final class DBHelper {

    private DBHelper() {
        throw new AssertionError();
    }

    public static String bookmarkIdFromContext(ProcessorContext context) {
        return context.requestHeaders().get(MessageConstants.BOOKMARK_ID);
    }
}

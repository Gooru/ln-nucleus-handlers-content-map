package org.gooru.nucleus.handlers.contentmap.processors.utils;

import java.util.UUID;

import org.gooru.nucleus.handlers.contentmap.constants.MessageConstants;
import org.gooru.nucleus.handlers.contentmap.processors.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ashish on 13/2/17.
 */
public final class ValidationHelperUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorContext.class);
    private static final int UUID_LENGTH = 36;

    public static boolean validateUser(String userId) {
        return !(userId == null || userId.isEmpty()) && (userId.equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)
            || validateUuid(userId));
    }

    public static boolean validateId(String id) {
        return !(id == null || id.isEmpty()) && validateUuid(id);
    }

    private static boolean validateEmail(String email) {
        // Rudimentary check for presence of @
        return email.indexOf('@') != -1;
    }

    private ValidationHelperUtils() {
        throw new AssertionError("Should not instantiate");
    }

    private static boolean validateUuid(String uuidString) {
        if (uuidString == null || uuidString.isEmpty() || uuidString.length() != UUID_LENGTH) {
            return false;
        }
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}

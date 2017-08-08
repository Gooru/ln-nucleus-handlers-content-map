package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators;

import java.util.Set;

/**
 * Created by ashish on 13/2/17.
 */
public interface FieldSelector {
    Set<String> allowedFields();

    default Set<String> mandatoryFields() {
        return null;
    }
}

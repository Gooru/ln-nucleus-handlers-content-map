package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.validators;

/**
 * Created by ashish on 13/2/17.
 */
public interface ValidatorRegistry {
    FieldValidator lookupValidator(String fieldName);

    default FieldValidator noopSuccessValidator(String fieldName) {
        return (n) -> true;
    }

    default FieldValidator noopFailedValidator(String fieldName) {
        return (n) -> false;
    }
}

package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.converters;

/**
 * Created by ashish on 13/2/17.
 */
public interface ConverterRegistry {
    FieldConverter lookupConverter(String fieldName);
}

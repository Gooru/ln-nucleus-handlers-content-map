package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.formatter;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 * Created by ashish on 13/2/17.
 */
public interface JsonFormatter {

    <T extends Model> String toJson(T model);

    <T extends Model> String toJson(LazyList<T> modelList);
}

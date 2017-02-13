package org.gooru.nucleus.handlers.contentmap.processors.repositories.activejdbc.formatter;

import java.util.List;

/**
 * Created by ashish on 13/2/17.
 */
public final class JsonFormatterBuilder {

    private JsonFormatterBuilder() {
        throw new AssertionError();
    }

    public static JsonFormatter buildSimpleJsonFormatter(boolean pretty, List<String> attributes) {

        return new SimpleJsonFormatter(pretty, attributes);
    }
}

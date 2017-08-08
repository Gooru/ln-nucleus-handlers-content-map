package org.gooru.nucleus.handlers.contentmap.bootstrap.startup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gooru.nucleus.handlers.contentmap.app.components.AppConfiguration;
import org.gooru.nucleus.handlers.contentmap.app.components.DataSourceRegistry;
import org.gooru.nucleus.handlers.contentmap.app.components.UtilityManager;

/**
 * @author ashish on 13/2/17.
 */
public class Initializers implements Iterable<Initializer> {

    private final Iterator<Initializer> internalIterator;

    public Initializers() {
        List<Initializer> initializers = new ArrayList<>();
        initializers.add(DataSourceRegistry.getInstance());
        initializers.add(AppConfiguration.getInstance());
        initializers.add(UtilityManager.getInstance());
        internalIterator = initializers.iterator();
    }

    @Override
    public Iterator<Initializer> iterator() {
        return new Iterator<Initializer>() {

            @Override
            public boolean hasNext() {
                return internalIterator.hasNext();
            }

            @Override
            public Initializer next() {
                return internalIterator.next();
            }

        };
    }

}

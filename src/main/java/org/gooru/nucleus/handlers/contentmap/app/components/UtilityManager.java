package org.gooru.nucleus.handlers.contentmap.app.components;

import org.gooru.nucleus.handlers.contentmap.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.contentmap.bootstrap.startup.Initializer;
import org.gooru.nucleus.libs.tenant.bootstrap.TenantInitializer;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 13/2/17.
 */
public class UtilityManager implements Initializer, Finalizer {
    private static final UtilityManager ourInstance = new UtilityManager();

    public static UtilityManager getInstance() {
        return ourInstance;
    }

    private UtilityManager() {
    }

    @Override
    public void finalizeComponent() {

    }

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        TenantInitializer.initialize(DataSourceRegistry.getInstance().getDefaultDataSource());
    }

}

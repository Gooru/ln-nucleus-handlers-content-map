package org.gooru.nucleus.handlers.contentmap.app.components;

import org.gooru.nucleus.handlers.contentmap.bootstrap.startup.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 13/2/17.
 */
public final class AppConfiguration implements Initializer {
    private static final String APP_CONFIG_KEY = "app.configuration";
    private static final String KEY = "__KEY__";
    private static final String CLASS_END_DATE_KEY = "class.end.date";
    private static final JsonObject configuration = new JsonObject();
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);
    private static final String POPULATE_DUMMY_EMAIL_KEY = "populate.dummy.email";
    private static final String DUMMY_EMAIL_DOMAIN_KEY = "dummy.email.domain";

    public static AppConfiguration getInstance() {
        return Holder.INSTANCE;
    }

    private volatile boolean initialized = false;

    private AppConfiguration() {
    }

    @Override
    public void initializeComponent(Vertx vertx, JsonObject config) {
        if (!initialized) {
            synchronized (Holder.INSTANCE) {
                if (!initialized) {
                    JsonObject appConfiguration = config.getJsonObject(APP_CONFIG_KEY);
                    if (appConfiguration == null || appConfiguration.isEmpty()) {
                        LOGGER.warn("App configuration is not available");
                    } else {
                        configuration.put(KEY, appConfiguration);
                        initialized = true;
                    }
                }
            }
        }
    }

    public String getClassEndDate() {
        return configuration.getJsonObject(KEY).getString(CLASS_END_DATE_KEY);
    }

    public boolean getPopulateDummyEmail() {
        return configuration.getJsonObject(KEY).getBoolean(POPULATE_DUMMY_EMAIL_KEY);
    }

    public String getDummyEmailDomain() {
        return configuration.getJsonObject(KEY).getString(DUMMY_EMAIL_DOMAIN_KEY);
    }

    private static final class Holder {
        private static final AppConfiguration INSTANCE = new AppConfiguration();
    }

}

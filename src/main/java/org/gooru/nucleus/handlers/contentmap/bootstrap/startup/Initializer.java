package org.gooru.nucleus.handlers.contentmap.bootstrap.startup;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 13/2/17.
 */

public interface Initializer {

    void initializeComponent(Vertx vertx, JsonObject config);

}

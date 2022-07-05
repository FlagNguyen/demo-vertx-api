package controller.health

import app.AppConfig
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.VertxController

/**
 * Check server still alive ?
 * If alive, response OK
 */
@InheritConstructors
class HealthLiveness extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        String responseMessage = "OK"

        writeJson(response, 200, responseMessage)
    }
}

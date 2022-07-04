package exception

import app.AppConfig
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.VertxController

@InheritConstructors
class NotFound extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        Map resultMap = [
                status_code: 404,
                error      : [
                        message: "Resource not found"
                ]
        ]

        writeJson(response, 404, resultMap)
    }
}

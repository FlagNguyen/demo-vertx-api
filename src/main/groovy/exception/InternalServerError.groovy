package exception

import app.AppConfig
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.exception.ExceptionUtils
import vertx.VertxController

@InheritConstructors
class InternalServerError extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        boolean debug = request.getParam("debug")?.equalsIgnoreCase("true")
        Map resultMap = [
                status: 500,
                error : [
                        message           : context.failure().message,
                        root_cause_message: debug ? ExceptionUtils.getRootCauseMessage(context.failure()) : null,
                        detail            : debug ? ExceptionUtils.getStackTrace(context.failure()) : null
                ]
        ]

        if (response.bytesWritten() <= 0) {
            writeJson(response, 500, resultMap)
        }
    }
}

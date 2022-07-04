package controller

import activemq.QueueReceiver
import app.AppConfig
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class DequeueMessage extends VertxController<AppConfig> {
    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            QueueReceiver queueReceiver = new QueueReceiver(config)
            List messages = queueReceiver.dequeue()
            writeJson(response, 200, new JsonResponse(data: messages))
        } catch (e) {
            log.error("Error when dequeue: $e")
        }
    }
}

package controller

import activemq.QueueSender
import app.AppConfig
import dao.entity.Error
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class EnqueueMessage extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            QueueSender queueSender = new QueueSender(config)
            String jsonMessage = context.getBodyAsString()
            queueSender.enqueue(jsonMessage)

            writeJson(response, 200, jsonMessage)
            log.info("Enqueue successfully "+jsonMessage)
        } catch (e) {
            log.error("Error when enqueue: $e")
            writeJson(response, 500, new JsonResponse<Error>(
                    data: new Error("500", "$e")
            ))
        }
    }
}

package controller

import activemq.QueueSender
import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import dao.entity.Teacher
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.Validator
import vertx.VertxController

@InheritConstructors
@Slf4j
class EnqueueMessage extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {
        Teacher teacherJsonObject = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)
        Validator.validateTeacherRequestAndReturnMessage(teacherJsonObject)
        context.put("data", context.getBodyAsString())
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            QueueSender queueSender = new QueueSender(config)
            String jsonMessage = context.get("data")
            queueSender.enqueue(jsonMessage)

            writeJson(response, 200, [data: jsonMessage])
            log.info("Enqueue successfully " + jsonMessage)
        } catch (e) {
            log.error("Error when enqueue: $e")
            writeJson(response, 500, [status_error: "500", message_error: "$e"])
        }
    }
}

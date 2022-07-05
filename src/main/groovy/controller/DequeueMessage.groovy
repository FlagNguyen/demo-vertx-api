package controller

import activemq.QueueReceiver
import app.AppConfig
import dao.entity.Teacher
import dao.entity.TeacherCollection
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
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            QueueReceiver queueReceiver = new QueueReceiver(config)
            List<Teacher> teachersDequeuedMessages = queueReceiver.dequeue()
            collection.insertManyModel(teachersDequeuedMessages)
            log.debug("Dequeue and insert new document into database successfully")
            writeJson(response, 200, new JsonResponse(data: teachersDequeuedMessages))
        } catch (e) {
            log.error("Error when dequeue: $e")
        }
    }
}

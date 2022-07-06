package controller

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import dao.entity.Error
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.Validator
import vertx.JsonResponse
import vertx.VertxController

/**
 * Enqueue teacher object messages to queue
 */
@InheritConstructors
@Slf4j
class CreateTeacher extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            //Convert Json to Teacher Object
            Teacher newTeacher = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)

            //Validate new teacher object and response error if necessary
            List errorMessage = Validator.validateTeacherRequestAndReturnMessage(newTeacher)
            if (errorMessage.contains("LackField")) {
                writeJson(response, 400, new Error("400", "Lack of required fields"))
                log.debug("Lack of required fields")
                return
            }
            if (errorMessage.contains("WrongFormatEmail")) {
                writeJson(response, 400, new Error("400", "Wrong email format"))
                log.debug("Wrong email format")
                return
            }

            //Add new teacher into database
            Teacher addedTeacher = collection.insertOneModel(newTeacher).join()

            //Response added teacher and logging
            writeJson(response, 200, new JsonResponse<>(data: addedTeacher))
            log.debug("Add successfully $newTeacher ")

        } catch (e) {
            log.error("Error when creating: $e")
            writeJson(response, 400,
                    new JsonResponse<Error>(data: new Error("400", "Error when insert teacher")))
        }
    }

}

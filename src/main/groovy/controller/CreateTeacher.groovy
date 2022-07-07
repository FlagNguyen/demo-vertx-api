package controller

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.Validator
import vertx.VertxController

/**
 * Create new teacher and save into database
 * Json input example:
 * {"teacher_name": "Mr. Van Test",
 "email": "123123@gmail.com"}
 */
@InheritConstructors
@Slf4j
class CreateTeacher extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {
        Teacher newTeacher = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)
        Validator.validateTeacherRequestAndReturnMessage(newTeacher)
        context.put("data", newTeacher)
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            //Convert Json to Teacher Object
            Teacher newTeacher = context.get("data")

            //Add new teacher into database
            Teacher addedTeacher = addNewTeacher(newTeacher)

            //Response added teacher and logging
            writeJson(response, 200, [data: addedTeacher])
            log.info("Add successfully $newTeacher ")

        } catch (e) {
            log.error("Error when creating: $e")
            writeJson(response, 400, [status_error: "400", message_error: "Error when insert teacher"])
        }
    }

    Teacher addNewTeacher(Teacher newTeacher) {
        return collection.insertOneModel(newTeacher).join()
    }

}

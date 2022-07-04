package controller

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import entity.Error
import entity.Teacher
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.SampleTeacherData
import util.Validator
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class CreateTeacher extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            Teacher newTeacher = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)
            List errorMessage = Validator.validateTeacherRequestAndReturnMessage(newTeacher)
            if (errorMessage.contains("DuplicateID")) {
                writeJson(response, 400, new Error("400", "This id is existed"))
                log.debug("This id $newTeacher.teacherID already exist")
                return
            }
            if (errorMessage.contains("WrongFormatEmail")) {
                writeJson(response, 400, new Error("400", "Wrong email format"))
                log.debug("Wrong email format")
                return
            }

            JsonResponse<Teacher> teacherJsonResponse = new JsonResponse<>(
                    data: newTeacher
            )

            SampleTeacherData.TEACHER_BY_ID.put(newTeacher.teacherID, newTeacher)
            writeJson(response, 200, teacherJsonResponse)
            log.debug("Add successfully $newTeacher ")

        } catch (e) {
            log.error("Error when creating: $e")
            writeJson(response,400, e)
        }

    }

}

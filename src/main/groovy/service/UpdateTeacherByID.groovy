package service

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
class UpdateTeacherByID extends VertxController<AppConfig> {


    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        Teacher updateTeacher = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)

        List errorMessages = Validator.validateTeacherRequestAndReturnMessage(updateTeacher)
        if (errorMessages.contains("WrongFormatEmail")) {
            writeJson(response, 400,
                    new JsonResponse<Error>(data: new Error("400", "Wrong Email Format")))
            log.error("Wrong Email Format")
            return
        }
        if (!SampleTeacherData.TEACHER_BY_ID.containsKey(updateTeacher.teacherID)) {
            writeJson(response, 400,
                    new JsonResponse<Error>(data: new Error("400", "This id doesn't existed")))
            log.error("This id doesn't existed")
            return
        }

        writeJson(response, 200, new JsonResponse<Teacher>(data: updateTeacher))
        SampleTeacherData.TEACHER_BY_ID.replace(updateTeacher.teacherID, updateTeacher)
        log.debug("Update successfully teacher's id: $updateTeacher.teacherID")
    }
}

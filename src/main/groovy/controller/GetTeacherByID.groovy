package controller

import app.AppConfig
import entity.Error
import entity.Teacher
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.SampleTeacherData
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
class GetTeacherByID extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        String teacherIdParam = request.getParam("id")

        Teacher foundTeacher = SampleTeacherData.TEACHER_BY_ID.get(teacherIdParam)
        if (foundTeacher == null) {
            writeJson(response, 404, new JsonResponse<Error>(
                    data: new Error("404", "Can't found this id")
            ))
            return
        }

        writeJson(response, 200, new JsonResponse<>(data: foundTeacher))
    }


}

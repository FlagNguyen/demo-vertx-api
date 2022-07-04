package controller


import app.AppConfig
import entity.Teacher
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.SampleTeacherData
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
class GetAllTeacher extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {

        JsonResponse<List<Teacher>> jsonResponse = new JsonResponse<>(
                data: SampleTeacherData.TEACHER_BY_ID
        )

        writeJson(response,200,jsonResponse)
    }


}

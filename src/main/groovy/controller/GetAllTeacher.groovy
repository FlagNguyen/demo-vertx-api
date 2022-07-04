package controller


import app.AppConfig
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.bson.conversions.Bson
import util.SampleTeacherData
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
class GetAllTeacher extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

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

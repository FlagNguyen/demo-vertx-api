package controller

import app.AppConfig
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
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
        writeJson(response, 200, new JsonResponse<>(
                data: collection.findModels().intoModels().join()
        ))
    }
}

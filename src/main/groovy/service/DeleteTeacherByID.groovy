package service

import app.AppConfig
import entity.Error
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import util.SampleTeacherData
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class DeleteTeacherByID extends VertxController<AppConfig> {

    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            String teacherIdParam = context.request().getParam("id")

            if (!SampleTeacherData.TEACHER_BY_ID.containsKey(teacherIdParam)) {
                writeJson(response, 404,
                        new JsonResponse<Error>(data: new Error("404", "Can't found this id")))
                log.error("Can't found this id")
                return
            }

            SampleTeacherData.TEACHER_BY_ID.remove(teacherIdParam)
            writeJson(response, 200, "Delete teacher's id $teacherIdParam successfully")
            log.info("Delete teacher's id $teacherIdParam successfully")
        } catch (e) {
            log.error("Error when deleting: $e")
        }
    }
}

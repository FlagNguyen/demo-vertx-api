package controller

import app.AppConfig
import dao.entity.Error
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.bson.types.ObjectId
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class GetTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        //Get input teacher id parameter
        ObjectId teacherIdParam = new ObjectId(request.getParam("id"))

        //Get teacher object in database which has input id parameter
        Teacher foundTeacher = collection.getModel(teacherIdParam).join()

        //Check this teacher is existed or not
        if (foundTeacher == null) {
            writeJson(response, 404, new JsonResponse<Error>(
                    data: new Error("404", "Can't found this id")
            ))
            return
        }

        //Logging and response
        log.info("Found Teacher: $foundTeacher")
        writeJson(response, 200, new JsonResponse<>(data: foundTeacher))
    }
}

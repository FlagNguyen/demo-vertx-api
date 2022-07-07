package controller

import app.AppConfig
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.Validate
import org.bson.types.ObjectId
import util.Validator
import vertx.VertxController

@InheritConstructors
@Slf4j
class GetTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {
        String id = context.request().getParam("id")
        Validate.isTrue(ObjectId.isValid(id ?: ""), "id not valid")
        Validate.isTrue(Validator.checkExistedTeacher(new ObjectId(id), collection), "Can't found this id")
        context.put("id", new ObjectId(id))
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        //Get input teacher id parameter
        ObjectId teacherIdParam = context.get("id") as ObjectId

        //Get teacher object in database which has input id parameter
        Teacher foundTeacher = collection.getModel(teacherIdParam).join()

        //Check this teacher is existed or not
        if (foundTeacher == null) {
            writeJson(response, 404, [status_code: "404", message: "Can't found this id"])
            return
        }

        //Logging and response
        log.info("Found Teacher: $foundTeacher")
        writeJson(response, 200, [data: foundTeacher])
    }
}

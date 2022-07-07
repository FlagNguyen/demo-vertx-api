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
import org.bson.Document
import org.bson.types.ObjectId
import util.Validator
import vertx.VertxController

@InheritConstructors
@Slf4j
class DeleteTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {
        String id = context.request().getParam("id")
        Validate.isTrue(ObjectId.isValid(id), "id not valid")
        Validate.isTrue(Validator.checkExistedTeacher(new ObjectId(id), collection), "Can't found this id")
        context.put("id", new ObjectId(id))
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            //Get teacher id param
            ObjectId teacherIdParam = context.get("id")

            //Delete in database
            Teacher deletedTeacher = deleteTeacher(teacherIdParam)

            //Response successfully
            writeJson(response, 200, "Delete teacher's id $teacherIdParam successfully")
            log.info("Delete teacher's id $teacherIdParam successfully")

        } catch (e) {
            log.error("Error when deleting: $e")
        }
    }

    Teacher deleteTeacher(ObjectId deleteTeacherId) {
        return collection.findOneAndDeleteModel(["_id", deleteTeacherId] as Document).join()
    }

}

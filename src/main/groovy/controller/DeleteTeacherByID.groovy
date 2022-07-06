package controller

import app.AppConfig
import dao.entity.Error
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.bson.Document
import org.bson.types.ObjectId
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class DeleteTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection

    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        try {
            //Get teacher id param
            ObjectId teacherIdParam = new ObjectId(request.getParam("id"))

            //Validate if this id is existed
            if (collection.getModel(teacherIdParam) == null) {
                writeJson(response, 404,
                        new JsonResponse<Error>(data: new Error("404", "Can't found this id")))
                log.error("Can't found this id")
                return
            }

            //Create filter
            Document filter = new Document("_id", teacherIdParam)

            //Delete in database
            collection.findOneAndDeleteModel(filter).join()

            //Response successfully
            writeJson(response, 200, "Delete teacher's id $teacherIdParam successfully")
            log.info("Delete teacher's id $teacherIdParam successfully")

        } catch (e) {
            log.error("Error when deleting: $e")
        }
    }

}

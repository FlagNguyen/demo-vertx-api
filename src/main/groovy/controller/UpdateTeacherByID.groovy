package controller

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import dao.entity.Error
import dao.entity.Teacher
import dao.entity.TeacherCollection
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.bson.Document
import org.bson.types.ObjectId
import util.Validator
import vertx.JsonResponse
import vertx.VertxController

@InheritConstructors
@Slf4j
class UpdateTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection


    @Override
    void validate(RoutingContext context) {

    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        //Get id param and body of request
        ObjectId updateTeacherId = new ObjectId(request.getParam("id"))
        Teacher updateTeacherRequest = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)

        //Check this teacher id still existed
        if (!checkExistedTeacher(updateTeacherId)) {
            writeJson(response, 404, new JsonResponse<Error>(
                    data: new Error("404", "Can't found this id")
            ))
            return
        }

        //Validate body request
        List errorMessages = Validator.validateTeacherRequestAndReturnMessage(updateTeacherRequest)
        if (errorMessages.contains("WrongFormatEmail")) {
            writeJson(response, 400,
                    new JsonResponse<Error>(data: new Error("400", "Wrong Email Format")))
            log.error("Wrong Email Format")
            return
        }
        if (errorMessages.contains("LackField")) {
            writeJson(response, 400, new Error("400", "Lack of required fields"))
            log.debug("Lack of required fields")
            return
        }

        //Create filter and update document
        Document filter = new Document("_id", updateTeacherId)
        Document update = new Document("\$set", collection.MAPPER.toDocument(updateTeacherRequest))

        //Update into database and return teacher object
        Teacher updatedTeacher = collection.findOneAndUpdateModel(filter, update).join()


        //Response updated teacher and logging
        writeJson(response, 200, new JsonResponse<Teacher>(data: updatedTeacher))
        log.debug("Update successfully teacher's id: $updateTeacherRequest.teacherID")
    }

    /**
     *
     * @param teacherIdParam
     * @return true if this teacher existed and false if it not
     */
    boolean checkExistedTeacher(ObjectId teacherIdParam) {
        //Get teacher object in database which has input id parameter
        Teacher foundTeacher = collection.getModel(teacherIdParam).join()

        //Check this teacher is existed or not
        if (foundTeacher == null) {
            return false
        }
        return true
    }
}

package controller

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
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
class UpdateTeacherByID extends VertxController<AppConfig> {
    TeacherCollection collection = config.teacherCollection


    @Override
    void validate(RoutingContext context) {
        String id = context.request().getParam("id")
        Validate.isTrue(ObjectId.isValid(id), "id not valid")
        Validate.isTrue(Validator.checkExistedTeacher(new ObjectId(id), collection), "Can't found this id")
        context.put("id", new ObjectId(id))

        Teacher updateTeacherRequest = ObjectMapper.newInstance().readValue(context.getBodyAsString(), Teacher.class)
        Validator.validateTeacherRequestAndReturnMessage(updateTeacherRequest)
        context.put("data", updateTeacherRequest)
    }

    @Override
    void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response) {
        //Get id param and body of request
        ObjectId updateTeacherId = context.get("id")
        Teacher updateTeacherRequest = context.get("data")

        //Update into database and return teacher object
        Teacher updatedTeacher = updateTeacher(updateTeacherId, updateTeacherRequest)

        //Response updated teacher and logging
        writeJson(response, 200, [data: updatedTeacher])
        log.info("Update successfully teacher's id: $updateTeacherRequest.teacherID")
    }

    Teacher updateTeacher(ObjectId updateTeacherId, Teacher updateTeacher) {
        //Create filter and update document
        Document filter = ["_id": updateTeacherId] as Document
        Document update = ["\$set": collection.MAPPER.toDocument(updateTeacher)] as Document

        //Update into database and return teacher object
        return collection.findOneAndUpdateModel(filter, update).join()
    }

}

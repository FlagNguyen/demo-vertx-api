package verticle

import com.fasterxml.jackson.databind.ObjectMapper
import entity.Teacher
import groovy.util.logging.Slf4j
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import org.apache.commons.validator.routines.EmailValidator
import entity.Error
import java.util.stream.Collectors

@Slf4j
class TeacherVerticle extends AbstractVerticle {

    ObjectMapper objectMapper = new ObjectMapper()

    /**
     * Map contain:
     * Key: Teacher ID <String>
     * Value: Teacher object
     */
    private Map<String, Teacher> teachersByID = new HashMap<>()

    void createSampleData() {
        teachersByID.put("1", new Teacher("1", "Mr. Obama", "obama@gmail.com"))
        teachersByID.put("2", new Teacher("2", "Mr. Donald Trump", "trump@gmail.com"))
        teachersByID.put("3", new Teacher("3", "Mr. Joe Biden", "biden@gmail.com"))
        log.info("Create sample data successfully")
    }

    @Override
    void start(Promise<Void> startPromise) throws Exception {
        createSampleData()
        Router router = Router.router(vertx)
        router.get("/api/teachers").handler(this.&getAllTeachers)
        router.get("/api/teachers/:id").handler(this.&getTeacherByID)
        router.route("/api/teachers*").handler(BodyHandler.create())
        router.post("/api/teachers").handler(this.&addNewTeacher)
        router.put("/api/teachers").handler(this.&updateTeacherByID)
        router.delete("/api/teachers/:id").handler(this.&deleteTeacherByID)
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", 8080),
                        { result ->
                            if (result.succeeded()) {
                                startPromise.complete()
                            } else {
                                startPromise.fail(result.cause())
                            }
                        })
    }

    /**
     * Return list Json of teachers
     * @param routingContext
     */
    private void getAllTeachers(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
        response.putHeader("content-type", "application/json;charset=UTF-8")
        response.end(objectMapper.writeValueAsString(teachersByID.values()))
        log.debug("Get all teachers successfully")
    }

    /**
     * return teacher by input teacher id parameter
     * @example-GET: http://localhost:8080/api/teachers/3
     * @param routingContext
     */
    private void getTeacherByID(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
        response.putHeader("content-type", "application/json;charset=UTF-8")

        String teacherIdParam = routingContext.request().getParam("id")
        try {
            Teacher foundTeacher = teachersByID.get(teacherIdParam)
            if (foundTeacher == null) {
                routingContext.response()
                        .setStatusCode(400)
                        .end(objectMapper
                                .writeValueAsString(new Error("001", "Can't found this id")))
                log.debug("Can't found this teacher")
                return
            }
            routingContext.response().setStatusCode(200).end(objectMapper.writeValueAsString(foundTeacher))
        } catch (e) {
            e.printStackTrace()
            routingContext.response().setStatusCode(400).end()
        }
    }


    /**
     * Method add new teacher by input Json
     * @emxample-POST: http://localhost:8080/api/teachers
     * @example-Json: {
     "teacherID": "abc",
     "teacherName": "Mr. Test",
     "email": "test@gmail.com"
     }
     * @param routingContext
     */
    private void addNewTeacher(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
        response.putHeader("content-type", "application/json;charset=UTF-8")
        try {
            //Convert input Json into Teacher object
            Teacher newTeacher = Json.decodeValue(routingContext.getBody(), Teacher.class)

            List errorMessages = validateTeacherRequestAndReturnMessage(newTeacher)
            if (errorMessages.contains("DuplicateID")) {
                response.end(objectMapper.
                        writeValueAsString(new Error("003", "This id is existed")))
                log.debug("This id is existed")
                return
            }
            if (errorMessages.contains("WrongFormatEmail")) {
                response.end(objectMapper.
                        writeValueAsString(new Error("002", "Wrong email format")))
                log.debug("Wrong email format")
                return
            }
            teachersByID.put(newTeacher.teacherID, newTeacher)
            routingContext.response().setStatusCode(200).end(objectMapper.writeValueAsString(newTeacher))
            log.debug("Add new successfully $newTeacher")
        } catch (e) {
            log.error("Error when add teacher: $e")
            response.end(e.getMessage())
        }
    }

    /**
     * Method validate input new teacher object
     * @validation: ID not duplicate & follow email format
     * @param newTeacher
     * @return List error key messages
     */
    private List validateTeacherRequestAndReturnMessage(Teacher newTeacher) {
        List outputErrorMessages = new ArrayList()

        //Get teacher ID list for checking duplicate
        List<String> teacherIDList = teachersByID.values()
                .stream()
                .map({ teacher -> teacher.teacherID })
                .collect(Collectors.toList())

        if (teacherIDList.contains(newTeacher.getTeacherID())) {
            outputErrorMessages.add("DuplicateID")
        }

        if (!EmailValidator.getInstance().isValid(newTeacher.getEmail())) {
            outputErrorMessages.add("WrongFormatEmail")
        }
        return outputErrorMessages
    }

    /**
     * Method update teacher by id
     * @input Json with id which teacher will be updated
     * @param routingContext
     */
    private void updateTeacherByID(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
        response.putHeader("content-type", "application/json;charset=UTF-8")
        try {
            Teacher updateTeacher = Json.decodeValue(routingContext.getBody(), Teacher.class)

            Teacher foundTeacher = teachersByID.get(updateTeacher.teacherID)
            if (foundTeacher == null) {
                response.end(objectMapper
                        .writeValueAsString(new Error("001", "Can't found this id")))
                log.debug("This id doesn't existed")
                return
            }
            if (!EmailValidator.getInstance().isValid(updateTeacher.email)) {
                response.end(objectMapper.
                        writeValueAsString(new Error("002", "Wrong email format")))
                log.debug("Wrong email format")
                return
            }

            teachersByID.replace(updateTeacher.teacherID, updateTeacher)
            log.debug("Update successfully $updateTeacher")
            routingContext.response().setStatusCode(200).end(objectMapper.writeValueAsString(updateTeacher))
        } catch (e) {
            log.error("Error when update teacher: $e")
            response.end("Delete failure. Error: " + e)
        }
    }

    /**
     * Method delete teacher by id parameter
     * @example-url: http://localhost:8080/api/teachers/abc (with "/abc" is id param)
     * @param routingContext
     */
    private void deleteTeacherByID(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
        response.putHeader("content-type", "application/json;charset=UTF-8")

        try {
            String teacherIdParam = routingContext.request().getParam("id")

            if (teachersByID.get(teacherIdParam).equals(null)) {
                routingContext.response()
                        .setStatusCode(400)
                        .end(objectMapper
                                .writeValueAsString(new Error("001", "Can't found this id")))
                log.debug("This id doesn't existed")
                return
            }
            teachersByID.remove(teacherIdParam)
            response.end("Delete successfully teacher's id: " + teachersByID)
            log.debug("Delete successfully teacher's id: " + teacherIdParam)
        } catch (e) {
            log.error("Error when deleting teacher: $e")
            response.end("Delete failure. Error: " + e)
        }
    }
}

package app

import exception.InternalServerError
import exception.NotFound
import io.vertx.ext.web.handler.BodyHandler
import service.CreateTeacher
import service.DeleteTeacherByID
import service.GetAllTeacher
import service.GetTeacherByID
import service.UpdateTeacherByID
import service.health.HealthLiveness
import vertx.VertXServer

class RestServer extends VertXServer<AppConfig>{


    RestServer(AppConfig config) {
        super(config)
    }

    @Override
    void setUpRouter() {
        router.route("/api/teacher*").handler(BodyHandler.create())
        router.route().failureHandler(new InternalServerError(config))
        router.route().last().handler(new NotFound(config))
        router.get("/health/liveness").handler(new HealthLiveness(config))
        router.get("/api/teachers").handler(new GetAllTeacher(config))
        router.get("/api/teachers/:id").handler(new GetTeacherByID(config))
        router.post("/api/teachers").handler(new CreateTeacher(config))
        router.put("/api/teachers").handler(new UpdateTeacherByID(config))
        router.delete("/api/teachers/:id").handler(new DeleteTeacherByID(config))
    }
}

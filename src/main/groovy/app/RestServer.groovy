package app

import controller.DequeueMessage
import exception.InternalServerError
import exception.NotFound
import io.vertx.ext.web.handler.BodyHandler
import controller.CreateTeacher
import controller.DeleteTeacherByID
import controller.EnqueueMessage
import controller.GetAllTeacher
import controller.GetTeacherByID
import controller.UpdateTeacherByID
import controller.health.HealthLiveness
import vertx.VertXServer

/**
 * Create Vertx Server
 */
class RestServer extends VertXServer<AppConfig>{


    RestServer(AppConfig config) {
        super(config)
    }

    /**
     * Set up routes for CRUD api
     */
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
        router.post("/api/teachers/enqueue").handler(new EnqueueMessage(config))
        router.post("/api/teachers/dequeue").handler(new DequeueMessage(config))
    }
}

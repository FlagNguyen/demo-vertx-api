import io.vertx.core.Vertx
import verticle.SubjectVerticle
import verticle.TeacherVerticle

class Main {
    static void main(String[] args) {
        Vertx vertx=Vertx.vertx()
        vertx.deployVerticle(new TeacherVerticle())
    }
}
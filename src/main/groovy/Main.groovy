import app.AppConfig
import app.RestServer
import entity.Teacher
import groovy.util.logging.Slf4j
import util.ConfigFactory
import util.SampleTeacherData

@Slf4j
class Main {

    static void createSampleData(Map teachersByID) {
        teachersByID.put("1", new Teacher("1", "Mr. Obama", "obama@gmail.com"))
        teachersByID.put("2", new Teacher("2", "Mr. Donald Trump", "trump@gmail.com"))
        teachersByID.put("3", new Teacher("3", "Mr. Joe Biden", "biden@gmail.com"))
        log.info("Create sample data successfully")
    }

    static void main(String[] args) {
//        Vertx vertx=Vertx.vertx()
//        vertx.deployVerticle(new TeacherVerticle())

        try {
            createSampleData(SampleTeacherData.TEACHER_BY_ID)
            AppConfig appConfig = ConfigFactory.getConfig("application.yml", AppConfig)

            RestServer restServer = new RestServer(appConfig)

            restServer.start()
        } catch (e) {
            e.printStackTrace()
        }
    }
}
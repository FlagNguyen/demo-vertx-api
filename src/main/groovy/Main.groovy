import app.AppConfig
import app.RestServer
import groovy.util.logging.Slf4j
import util.ConfigFactory

@Slf4j
class Main {
    static void main(String[] args) {
        //Get configurations in file application.yml and convert into AppConfig Object
        AppConfig appConfig = ConfigFactory.getConfig("application.yml", AppConfig)

        RestServer restServer = new RestServer(appConfig)
        restServer.start()
    }
}
package vertx

import groovy.util.logging.Slf4j
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

@Slf4j
abstract class VertXServer<C extends VertXConfig> {
    C config

    final Router router
    final HttpServer httpServer

    VertXServer(C config) {
        this.config = config
        this.router = Router.router(config.vertx)
        this.httpServer = config.vertx.createHttpServer()

        setUpRouter()
    }

    abstract void setUpRouter()

    void start() {
        httpServer
                .requestHandler(router)
                .listen(config.httpPort, {
                    event ->
                        if (event.succeeded()) {
                            log.info("=== Rest server started at http://127.0.0.1:{} ===", httpServer.actualPort())
                        } else {
                            close()
                            throw new RuntimeException("Unable to start Rest server at http://127.0.0.1:" + httpServer.actualPort())
                        }
                })
    }

    void close() {
        httpServer.close()
        config.vertx.close()
    }
}

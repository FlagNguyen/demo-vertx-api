package verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

import java.util.function.Function

class SubjectVerticle extends AbstractVerticle {

    @Override
    void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx)
        router.get("/api/subjects").handler(this.&subjects)

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", 8080),
                        { result ->
                            if (result.succeeded()) {
                                startPromise.complete()
                            } else {
                                startPromise.fail(result.cause())
                            }
                        }
                )
    }

    private void subjects(RoutingContext routingContext) {
        String[] subjects =
                ["Basic C#",
                 "Basic Java",
                 "Advanced Java",
                 "Java BackEnd",
                 "AI"
                ]
        routingContext.response()
                .putHeader("content-type", "application/jsoncharset=utf-8")
                .end(Json.encodePrettily(subjects))
    }

}

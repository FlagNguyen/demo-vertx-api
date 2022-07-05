package vertx

import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.Vertx

class VertXConfig {
    @JsonProperty("http.port")
    Integer httpPort

    Vertx vertx = Vertx.vertx()
}

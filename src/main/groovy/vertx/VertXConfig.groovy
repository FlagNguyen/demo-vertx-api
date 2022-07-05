package vertx

import com.fasterxml.jackson.annotation.JsonProperty
import io.vertx.core.Vertx

/**
 * Get port attribute in application.yml
 * And create VertX
 */
class VertXConfig {
    @JsonProperty("http.port")
    Integer httpPort

    Vertx vertx = Vertx.vertx()
}

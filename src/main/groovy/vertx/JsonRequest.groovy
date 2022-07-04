package vertx

import com.fasterxml.jackson.annotation.JsonIgnore
import io.vertx.ext.web.RoutingContext

/**
 * Get
 * @param <T>
 */
class JsonRequest<T> {
    @JsonIgnore
    RoutingContext context

    T data
}

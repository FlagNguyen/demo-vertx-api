package vertx

import com.fasterxml.jackson.annotation.JsonIgnore
import io.vertx.ext.web.RoutingContext

class JsonRequest<T> {
    @JsonIgnore
    RoutingContext context

    T data
}

package vertx

import com.fasterxml.jackson.annotation.JsonInclude

class JsonResponse<T> {
    Integer statusCode = 200

    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data
}

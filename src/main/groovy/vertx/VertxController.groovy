package vertx

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.RoutingContext
import org.apache.commons.lang3.Validate

/**
 * Abstract class for controller classes
 * @param <C>
 */
abstract class VertxController<C extends VertXConfig> implements Handler<RoutingContext> {
    C config

    VertxController(C config) {
        this.config = config
    }

    static final ObjectMapper MAPPER = new ObjectMapper().with {
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    }

    @Override
    void handle(RoutingContext routingContext) {
        this.validate(routingContext)
        this.handle(routingContext, routingContext.request(), routingContext.response())
    }

    abstract void validate(RoutingContext context)

    abstract void handle(RoutingContext context, HttpServerRequest request, HttpServerResponse response)

    static <T> JsonRequest<T> parseJson(RoutingContext context, Class<T> genericClazz) {
        try {
            JsonRequest<T> jsonRequest
            JavaType jsonRequestType = MAPPER.typeFactory.constructParametricType(JsonRequest, genericClazz)
            jsonRequest = MAPPER.readValue(context.body?.bytes, jsonRequestType)
            jsonRequest.context = context
            return jsonRequest
        } catch (IOException e) {
            throw new RuntimeException(e)
        }
    }

    /**
     * method response json data
     * @param response
     * @param statusCode
     * @param object
     */
    static void writeJson(HttpServerResponse response, Integer statusCode, Object object) {
        Validate.notNull(object, "Object must not be null")
        response.setStatusCode(statusCode).putHeader("Content-Type", "application/json;charset=UTF-8")

        if (object instanceof String) {
            response.end(object)
            return
        }

        if (object instanceof JsonResponse) {
            object.statusCode = statusCode
        }

        if (object instanceof Map) {
            if (object.status_code) {
                object.status_code = statusCode
            }
        }

        byte[] bytes = MAPPER.writeValueAsBytes(object)
        response.end(Buffer.buffer(bytes))
    }
}

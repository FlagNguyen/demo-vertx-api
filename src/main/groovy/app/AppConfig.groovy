package app

import com.fasterxml.jackson.annotation.JsonProperty
import vertx.VertXConfig

class AppConfig extends VertXConfig{
    // MongoDB
//    @JsonProperty("work-tools.mongodb.uri")
//    WorkToolCollection workToolCollection

    //ActiveMQ
    @JsonProperty("activemq.broker.url")
    String amqBrokerUrl

    @JsonProperty("activemq.username")
    String amqUserName

    @JsonProperty("activemq.password")
    String amqPassword

    @JsonProperty("activemq.queue")
    String amqQueue

    @JsonProperty("activemq.consumer-timeout")
    Long amqConsumerTimeOut
}

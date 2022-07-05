package app

import com.fasterxml.jackson.annotation.JsonProperty
import dao.entity.TeacherCollection
import vertx.VertXConfig

class AppConfig extends VertXConfig{
     //MongoDB
    @JsonProperty("mongodb.uri")
    TeacherCollection teacherCollection

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

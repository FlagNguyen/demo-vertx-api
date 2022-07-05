package activemq

import com.fasterxml.jackson.annotation.JsonProperty

import javax.jms.Session

class ActiveMQConfig {
    @JsonProperty("brokerUrl")
    protected String brokerUrl

    @JsonProperty("destination")
    protected String destination

    @JsonProperty("username")
    protected String username

    @JsonProperty("password")
    protected String password

    @JsonProperty("transacted")
    protected Boolean transacted = false

    @JsonProperty("acknowledge_mode")
    protected Integer acknowledgeMode = Session.AUTO_ACKNOWLEDGE

    @JsonProperty("consumer_timeout")
    protected Long consumerTimeOut
}

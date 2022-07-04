package activemq

import app.AppConfig
import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.ActiveMQSession
import org.apache.activemq.command.ActiveMQQueue

import javax.jms.JMSException
import javax.jms.MessageProducer
import javax.jms.TextMessage

class QueueSender {
    private ActiveMQConnection connection
    private ActiveMQConnectionFactory connectionFactory
    private ActiveMQSession session
    private MessageProducer messageProducer
    private ActiveMQQueue queue
    private ActiveMQConfig config

    QueueSender(AppConfig appConfig){
        ActiveMQConfig activeMQConfig = new ActiveMQConfig(
                brokerUrl: appConfig.amqBrokerUrl,
                username: appConfig.amqUserName,
                password: appConfig.amqPassword,
                destination: appConfig.amqQueue,
                consumerTimeOut: appConfig.amqConsumerTimeOut
        )
        this.config = activeMQConfig

        connectionFactory = new ActiveMQConnectionFactory(
                brokerURL: config.brokerUrl,
                userName: config.username,
                password: config.password
        )

        connection = connectionFactory.createConnection() as ActiveMQConnection
        session = connection.createSession(false, config.acknowledgeMode) as ActiveMQSession
        queue = session.createQueue(config.destination) as ActiveMQQueue
        messageProducer = session.createProducer(queue)

        connection.start()
    }

    void enqueue(String inputJsonObject) throws JMSException{
        TextMessage textMessage = session.createTextMessage(inputJsonObject)
        messageProducer.send(textMessage)
        if(session.transacted){
            session.commit()
        }
        close()
    }

    void close() {
        messageProducer.close()
        session.close()
        connection.close()
    }
}

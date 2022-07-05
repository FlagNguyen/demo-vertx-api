package activemq

import app.AppConfig
import org.apache.activemq.ActiveMQConnection
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.ActiveMQSession
import org.apache.activemq.command.ActiveMQQueue

import javax.jms.Message
import javax.jms.MessageConsumer
import javax.jms.TextMessage

class QueueReceiver {
    private ActiveMQConnection connection
    private ActiveMQConnectionFactory connectionFactory
    private ActiveMQSession session
    private MessageConsumer messageConsumer
    private ActiveMQQueue queue
    private ActiveMQConfig config

    QueueReceiver(AppConfig config) {
        ActiveMQConfig activeMQConfig = new ActiveMQConfig(
                brokerUrl: config.amqBrokerUrl,
                username: config.amqUserName,
                password: config.amqPassword,
                destination: config.amqQueue,
                consumerTimeOut: config.amqConsumerTimeOut
        )
        this.config = activeMQConfig

        connectionFactory = new ActiveMQConnectionFactory(
                brokerURL: this.config.brokerUrl,
                userName: this.config.username,
                password: this.config.password)
        connection = connectionFactory.createConnection() as ActiveMQConnection
        session = connection.createSession(this.config.transacted, this.config.acknowledgeMode) as ActiveMQSession
        queue = session.createQueue(this.config.destination) as ActiveMQQueue
        messageConsumer = session.createConsumer(queue)

        connection.start()
    }

    List<String> dequeue() {
        List<String> messages = new ArrayList()
        while (true) {
            Message message = messageConsumer.receive(config.consumerTimeOut)
            if (message == null) {
                break
            }
            if (message instanceof TextMessage) {
                messages.add(message.getText())
            }
            if (session.transacted) {
                session.commit()
            }
        }
        close()
        return messages
    }

    void close() {
        messageConsumer.close()
        session.close()
        connection.close()
    }
}

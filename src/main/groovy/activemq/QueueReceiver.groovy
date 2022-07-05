package activemq

import app.AppConfig
import com.fasterxml.jackson.databind.ObjectMapper
import dao.entity.Teacher
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

    /**
     * Constructor create connection, session, queue, producer as necessary by configuration
     * Start connection after create
     * @param config
     */
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

    /**
     * Dequeue all messages in queue
     * @return List of teachers which was be dequeued
     */
    List<Teacher> dequeue() {
        List<Teacher> receiveMessages = new ArrayList()
        while (true) {
            Message message = messageConsumer.receive(config.consumerTimeOut)
            if (message == null) {
                break
            }
            if (message instanceof TextMessage) {

                //Convert received Json message to Teacher object then add into output list
                Teacher teacher = ObjectMapper.newInstance().readValue(message.getText(), Teacher.class)
                receiveMessages.add(teacher)
            }
            if (session.transacted) {
                session.commit()
            }
        }
        close()
        return receiveMessages
    }

    /**
     * close all connection, session, producer
     */
    void close() {
        messageConsumer.close()
        session.close()
        connection.close()
    }
}

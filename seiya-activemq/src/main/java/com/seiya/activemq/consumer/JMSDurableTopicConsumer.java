package com.seiya.activemq.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 离线消息消费
 */
public class JMSDurableTopicConsumer {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.101:61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("Super-001");
            connection.start();

            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Topic destination = session.createTopic("MyTopic");
            // 创建消费者
            MessageConsumer consumer = session.createDurableSubscriber(destination, "Super-001");

            TextMessage textMessage = (TextMessage) consumer.receive();
            System.out.println(textMessage.getText());

            session.close();

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

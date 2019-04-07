package com.seiya.activemq.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * JMSTopicPruducer 消息生产者
 */
public class JMSTopicPruducer {

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.0.101:61616");
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            // 创建会话
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地
            Destination destination = session.createTopic("MyTopic");
            // 创建生产者
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT); // 消息持久化
            // 发送消息
            TextMessage textMessage = session.createTextMessage("测试离线消息2");
            producer.send(textMessage);
            // 消息确认
            session.commit();
            session.rollback();
            // 关闭会话
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

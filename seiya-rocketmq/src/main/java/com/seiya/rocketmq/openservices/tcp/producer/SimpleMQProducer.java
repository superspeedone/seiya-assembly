package com.seiya.rocketmq.openservices.tcp.producer;

import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.seiya.rocketmq.openservices.tcp.MqConfig;

import java.util.Date;
import java.util.Properties;

/**
 * MQ发送普通消息示例 Demo
 */
public class SimpleMQProducer {


    public static void main(String[] args) {
        // 设置属性 topic group_id 等
        Properties producerProperties = new Properties();
        producerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        producerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        producerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        producerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        // 创建消息生产者
        Producer producer = ONSFactory.createProducer(producerProperties);
        producer.start();
        System.out.println("Producer Started");

        // 循环发送消息
        for (int i = 0; i < 5; i++) {
            // 创建消息
            Message message = new Message(MqConfig.TOPIC, MqConfig.TAG, "mq send message test".getBytes());
            try {
                // 发送消息并接收消息发送结果
                SendResult sendResult = producer.send(message);
                assert sendResult != null;
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.TOPIC + " msgId is: " + sendResult.getMessageId());
            } catch (ONSClientException e) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                System.out.println(new Date() + " Send mq message failed! Topic is:" + MqConfig.TOPIC);
                e.printStackTrace();
            }
        }
    }
}

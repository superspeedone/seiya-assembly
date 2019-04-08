package com.seiya.rocketmq.openservices.tcp.consumer;

import com.aliyun.openservices.ons.api.*;
import com.seiya.rocketmq.openservices.tcp.MqConfig;

import java.util.Date;
import java.util.Properties;

/**
 * MQ 接收消息示例 Demo （默认为集群消费模式）
 */
public class SimpleConsumer {

    public static void main(String[] args) {
        // 设置属性 topic group_id 等
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        // consumerProperties.setProperty(PropertyKeyConst.MessageModel, MqConfig.NAMESRV_ADDR);
        // 创建消息消费者
        Consumer consumer = ONSFactory.createConsumer(consumerProperties);
        // 消息订阅
        consumer.subscribe(MqConfig.TRANSACTION_TOPIC, MqConfig.TAG, new MessageListener() {
            // MQ消息处理类
            @Override
            public Action consume(Message message, ConsumeContext consumeContext) {
                System.out.println(new Date() + " Receive message, Topic is:" + message.getTopic()
                        + ", MsgId is:" + message.getMsgID() + ", MsgContent:" + new String(message.getBody()));
                //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer start success.");

        //等待固定时间防止进程退出
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
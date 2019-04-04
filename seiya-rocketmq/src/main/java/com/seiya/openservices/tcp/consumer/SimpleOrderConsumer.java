package com.seiya.openservices.tcp.consumer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.seiya.openservices.tcp.MqConfig;

import java.util.Date;
import java.util.Properties;

/**
 * MQ 接收消息示例 Demo
 */
public class SimpleOrderConsumer {

    public static void main(String[] args) {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.ORDER_GROUP_ID);
        consumerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        consumerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        consumerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        // 集群订阅方式 (默认)
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.CLUSTERING);
        // 广播订阅方式
        // properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        OrderConsumer consumer = ONSFactory.createOrderedConsumer(consumerProperties);
        consumer.subscribe(MqConfig.ORDER_TOPIC, MqConfig.TAG,  new MessageOrderListener() {

            @Override
            public OrderAction consume(final Message message, final ConsumeOrderContext context) {
                System.out.println(new Date() + " Order Receive message, Topic is:" + message.getTopic()
                        + ", MsgId is:" + message.getMsgID() + ", MsgKey is:" + message.getKey()
                        + ", MsgShardingKey is:" + message.getShardingKey()
                        + ", MsgContent is:" + new String(message.getBody()));
                return OrderAction.Success;
            }
        });
        consumer.start();
        System.out.println("Order Consumer start success.");

        //等待固定时间防止进程退出
        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
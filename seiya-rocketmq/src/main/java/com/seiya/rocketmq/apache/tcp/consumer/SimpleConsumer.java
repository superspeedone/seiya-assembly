package com.seiya.rocketmq.apache.tcp.consumer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消息消费者
 */
public class SimpleConsumer {

    public static void main(String[] args) throws Exception {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MqConfig.CONSUMER_GROUP_ID);

        // Specify name server addresses.
        consumer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);

        // 消息重试
        // 对于无序消息（普通、定时、延时、事务消息），当消费者消费消息失败时，您可以通过设置返回状态达到消息重试的结果。
        // 无序消息的重试只针对集群消费方式生效；广播方式不提供失败重试特性，即消费失败后，失败消息不再重试，继续消费新的消息。
        // 消息监听器接口的实现中明确进行配置（三种方式任选一种）：1.返回 Action.ReconsumeLater （推荐）2.返回 Null 3.抛出异常
        // 消息队列 RocketMQ 默认允许每条消息最多重试 16 次，如果不重试，直接在监听接口中提交消息即可
        // 每次重试的间隔时间参考阿里云文档https://help.aliyun.com/document_detail/43490.html?spm=a2c4g.11186623.2.24.3369793fOsnxOK
        consumer.setMaxReconsumeTimes(20);

        // Subscribe one more more topics to consume.
        consumer.subscribe(MqConfig.TOPIC, "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }

}

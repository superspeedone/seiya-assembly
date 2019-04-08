package com.seiya.rocketmq.apache.tcp.consumer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 顺序消费
 */
public class OrderConsumer {

    public static void main(String[] args) throws Exception {
        // 开启消息轨迹跟踪
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MqConfig.CONSUMER_ORDER_GROUP_ID, Boolean.TRUE, "RMQ_SYS_TRACE_TOPIC");

        // Specify name server addresses.
        consumer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe(MqConfig.ORDER_TOPIC, "TagA || TagC || TagD");

        consumer.registerMessageListener(new MessageListenerOrderly() {

            Random random = new Random();

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                for (MessageExt msg : msgs) {
                    System.out.println(msg + ", content:" + new String(msg.getBody()));
                }
                try {
                    //模拟业务逻辑处理中...
                    TimeUnit.SECONDS.sleep(random.nextInt(10));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }

}

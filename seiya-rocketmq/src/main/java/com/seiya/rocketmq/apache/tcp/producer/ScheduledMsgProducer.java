package com.seiya.rocketmq.apache.tcp.producer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 延时消息/定时消息
 */
public class ScheduledMsgProducer {

    public static void main(String[] args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = new DefaultMQProducer(MqConfig.PRODUCER_GROUP_ID);
        // Specify name server addresses.
        producer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        // Launch producer
        producer.start();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(MqConfig.TOPIC, ("Hello scheduled message " + i).getBytes());
            // This message will be delivered to consumer 3 seconds later.
            message.setDelayTimeLevel(3);
            // Send the message
            producer.send(message);
        }

        // Shutdown producer after use.
        producer.shutdown();
    }

}

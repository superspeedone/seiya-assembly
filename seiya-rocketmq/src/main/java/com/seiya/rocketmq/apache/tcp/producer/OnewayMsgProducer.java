package com.seiya.rocketmq.apache.tcp.producer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 单向发送消息
 * 注册回调
 */
public class OnewayMsgProducer {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer (MqConfig.PRODUCER_GROUP_ID);
        // Specify name server addresses.
        producer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        //Launch the instance.
        producer.start();

        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(MqConfig.TOPIC /* Topic */,
                    MqConfig.TAG /* Tag */,
                    ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKeys("ORDERID_" + i);
            //Call send message to deliver message to one of brokers.
            // 由于在 oneway 方式发送消息时没有请求应答处理，一旦出现消息发送失败，
            // 则会因为没有重试而导致数据丢失。若数据不可丢，建议选用可靠同步或可靠异步发送方式。
            producer.sendOneway(msg);
        }

        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

}

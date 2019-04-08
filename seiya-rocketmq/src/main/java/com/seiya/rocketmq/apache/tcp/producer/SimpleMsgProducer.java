package com.seiya.rocketmq.apache.tcp.producer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 消息生产者
 */
public class SimpleMsgProducer {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer (MqConfig.PRODUCER_GROUP_ID);
        // Specify name server addresses. 设置 TCP 接入域名，进入控制台的实例管理页面的“获取接入点信息”区域查看
        producer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        producer.setSendMsgTimeout(3000);
        //Launch the instance.  在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可。
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(MqConfig.TOPIC /* Topic */,
                    // 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在消息队列 RocketMQ 的服务器过滤
                    MqConfig.TAG /* Tag */,
                    // 任何二进制形式的数据，消息队列 RocketMQ 不做任何干预，需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过阿里云服务器管理控制台查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKeys("ORDERID_" + i);
            // Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

}

package com.seiya.rocketmq.apache.tcp.producer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.CountDownLatch;

/**
 * 异步发送消息
 * 注册回调
 */
public class AsnycMsgProducer {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer (MqConfig.PRODUCER_GROUP_ID);
        // Specify name server addresses.
        producer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        //Launch the instance.
        producer.start();
        int messageCount = 10;
        // 由于消息是异步发送，提前关闭消费者实例会抛出异常
        // org.apache.rocketmq.client.exception.MQClientException: No route info of this topic异常）
        // 需要等到回调执行完之后关闭
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(MqConfig.TOPIC /* Topic */,
                    MqConfig.TAG /* Tag */,
                    ("Hello RocketMQ ").getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%s%n", sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();
                    throwable.printStackTrace();
                }
            });
        }
        // countDownLatch.await(5, TimeUnit.SECONDS); 可以设置等待时间，超过5秒，继续往下执行
        countDownLatch.await();  // 一直等待，直到计数器变成0，继续往下执行
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

}

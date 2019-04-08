package com.seiya.rocketmq.apache.tcp.producer;

import com.seiya.rocketmq.apache.tcp.MqConfig;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 顺序消息
 */
public class OrderMsgProducer {

    public static void main(String[] args) throws Exception {
        //Instantiate with a producer group name.  开启消息轨迹跟踪
        DefaultMQProducer producer = new DefaultMQProducer(MqConfig.PRODUCER_ORDER_GROUP_ID, Boolean.TRUE, "RMQ_SYS_TRACE_TOPIC");
        // Specify name server addresses.
        producer.setNamesrvAddr(MqConfig.NAMESRV_ADDR);
        //Launch the instance.
        producer.start();
        String[] tags = new String[]{"TagA", "TagC", "TagD"};
        // 订单列表
        List<OrderDemo> orderList =  new OrderMsgProducer().buildOrders();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < 12; i++) {
            Long shardingKey = orderList.get(i).getOrderId();
            String body = formater.format(new Date()) + " Hello RocketMQ " + orderList.get(i);
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(MqConfig.ORDER_TOPIC,
                    tags[i % tags.length],
                    "KEY" + i,
                    body.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送消息
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object shardingKey) {
                    // int select = Math.abs(arg.hashCode()); 阿里云使用的是hashcode
                    Long id = (Long) shardingKey;
                    long index = id % mqs.size();
                    return mqs.get((int) index);
                }
            }, shardingKey);  // 分区key为订单id

            System.out.printf("%s, body:%s%n", sendResult, body);
        }
        //server shutdown 生产者关闭的时候，自动会将生产组删除
        producer.shutdown();
    }

    /**
     * 生成模拟订单数据
     */
    private List<OrderDemo> buildOrders() {
        List<OrderDemo> orderList = new ArrayList<OrderDemo>();

        OrderDemo orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("创建");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("付款");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("推送");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103117235L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111039L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        orderDemo = new OrderDemo();
        orderDemo.setOrderId(15103111065L);
        orderDemo.setDesc("完成");
        orderList.add(orderDemo);

        return orderList;
    }

    public static class OrderDemo {
        private long orderId;
        private String desc;


        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "OrderDemo{" +
                    "orderId=" + orderId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

}

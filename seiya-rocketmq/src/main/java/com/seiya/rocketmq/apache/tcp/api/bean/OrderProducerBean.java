package com.seiya.rocketmq.apache.tcp.api.bean;

import com.seiya.rocketmq.apache.tcp.api.OrderProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code OrderProducerBean}用于将{@link OrderProducer}集成至Spring Bean中
 */
public class OrderProducerBean implements OrderProducer {
    /**
     * 需要注入该字段，指定构造{@code OrderProducer}实例的属性ßß
     *
     * @see OrderProducerBean#setProperties(Properties)
     */
    private Properties properties;

    private DefaultMQProducer orderProducer;

    protected final AtomicBoolean started = new AtomicBoolean(false);

    /**
     * 启动该{@code OrderProducer}实例，建议配置为Bean的init-method
     */
    @Override
    public void start() {
        if (null == this.properties) {
            throw new RuntimeException("properties not set");
        }

        this.orderProducer = new DefaultMQProducer(this.properties.getProperty("GroupId"));
        this.orderProducer.setNamesrvAddr(this.properties.getProperty("NameSrvAddr"));
        try {
            this.orderProducer.start();
            started.set(true);
        } catch (MQClientException e) {
            throw new RuntimeException("order producer start failed");
        }
    }

    /**
     * 关闭该{@code OrderProducer}实例，建议配置为Bean的destroy-method
     */
    @Override
    public void shutdown() {
        if (this.orderProducer != null) {
            this.orderProducer.shutdown();
            started.set(false);
        }
    }

    @Override
    public boolean isStarted() {
        return this.started.get();
    }

    @Override
    public boolean isClosed() {
        return !this.started.get();
    }

    @Override
    public SendResult send(final Message message, final String shardingKey) {

        try {
            return this.orderProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object shardingKey) {
                    int select = Math.abs(shardingKey.hashCode()); // 采用阿里云的分区算法
                    long index = select % mqs.size();
                    return mqs.get((int) index);
                }
            }, shardingKey);  // 分区key
        } catch (Exception e) {
            throw new RuntimeException("msg send failed");
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(final Properties properties) {
        this.properties = properties;
    }
}


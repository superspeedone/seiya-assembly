package com.seiya.rocketmq.openservices.tcp;

import com.seiya.rocketmq.apache.tcp.api.OrderProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) {

        /**
         * 生产者 Bean 配置在 producer.xml 中，可通过 ApplicationContext 获取或者直接注入到其他类（比如具体的 Controller）中
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("producer.xml");
        OrderProducer producer = (OrderProducer) context.getBean("producer");
        System.out.println(producer);
        producer.shutdown();
    }

}

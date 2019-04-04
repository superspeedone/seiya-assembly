package com.seiya.rpc.dubbo.consumer;

import com.seiya.rpc.dubbo.api.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 项目名称：seiya-activemq
 * 类名称：Consumer
 * 类描述：dubbo消费端
 * 创建人：yanweiwen
 * 创建时间：2019/4/3 20:26
 */
public class Consumer {

    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-consumer.xml"});
        context.start();

        DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法

        System.out.println(hello); // 显示调用结果

        System.in.read();

    }

}

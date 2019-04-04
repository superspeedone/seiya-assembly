package com.seiya.rpc.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 项目名称：seiya-activemq
 * 类名称：Provider
 * 类描述：dubbo服务端
 * 创建人：yanweiwen
 * 创建时间：2019/4/3 20:24
 */
public class Provider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/dubbo-demo-provider.xml"});
        context.start();
        System.in.read(); // 按任意键退出
    }

}

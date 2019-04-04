package com.seiya.rpc.dubbo.provider;

import com.alibaba.dubbo.rpc.RpcContext;
import com.seiya.rpc.dubbo.api.DemoService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：seiya-activemq
 * 类名称：DemoServiceImpl
 * 类描述：DemoServiceImpl /
 * 创建人：yanweiwen
 * 创建时间：2019/4/3 20:25
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }

}

package com.seiya.rocketmq.openservices.tcp;

/**
 * 项目名称：seiya-cache
 * 类名称：MqConfig
 * 类描述：MqConfig / 配置文件
 * 创建人：yanweiwen
 * 创建时间：2019/4/4 15:01
 */
public class MqConfig {

    /**
     * 启动测试之前请替换如下 XXX 为您的配置
     */
    public static final String TOPIC = "rocketmq-test";
    public static final String ORDER_TOPIC = "rocketmq-order-test";
    public static final String TRANSACTION_TOPIC = "rocketmq-transaction-test";
    public static final String GROUP_ID = "GID_ROCKETMQ_TEST";
    public static final String ORDER_GROUP_ID = "GID_ROCKETMQ_ORDER_TEST";
    public static final String TRANSACTION_GROUP_ID = "GID_ROCKETMQ_TRANSACTION_TEST";
    public static final String ACCESS_KEY = "LTAIOtpq4xqmSBBT";
    public static final String SECRET_KEY = "iIe2X1KtIdtaHerIDPnwf3JtdSgXRN";
    public static final String TAG = "mq_test_tag";

    /**
     * NAMESRV_ADDR, 请在mq控制台 https://ons.console.aliyun.com 通过"实例管理--获取接入点信息--TCP协议接入点"获取
     */
    public static final String NAMESRV_ADDR = "http://MQ_INST_1347783194184037_BamSQeHQ.mq-internet-access.mq-internet.aliyuncs.com:80";

}

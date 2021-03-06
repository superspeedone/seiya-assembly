package com.seiya.rocketmq.apache.tcp;

/**
 * 项目名称：seiya-cache
 * 类名称：MqConfig
 * 类描述：MqConfig / 配置文件
 * 创建人：yanweiwen
 * 创建时间：2019/4/4 15:01
 */
public class MqConfig {

    /**
     * Topic名称长度：限制64个字符，Topic名称长度不得超过该限制，否则会导致无法发送或者订阅。
     * 消息大小：限制4MB字节，消息大小不得超过该限制，否则消息会被丢弃。
     * 消息保存时间：限制3天，消息最多保留3天，超过时间将自动滚动删除。
     * 消费位点重置：限制3天，支持重置消费，3天之内任何时间点的消息。
     * 定时/延时消息的延时时长：限制40天，msg.setStartDeliverTime的参数可设置，40天内的任何时刻（单位毫秒），超过40天消息发送将失败
     */
    public static final String TOPIC = "rocketmq-test";
    public static final String ORDER_TOPIC = "rocketmq-order-test";
    public static final String BROADCAST_TOPIC = "rocketmq-broadcast-test";
    public static final String TRANSACTION_TOPIC = "rocketmq-transaction-test";
    public static final String CONSUMER_GROUP_ID = "GID_CONSUMER_TEST";
    public static final String CONSUMER_BROADCAST_GROUP_ID = "GID_CONSUMER_BROADCAST_TEST";
    public static final String CONSUMER_ORDER_GROUP_ID = "GID_CONSUMER_ORDER_TEST";
    public static final String CONSUMER_TRANSACTION_GROUP_ID = "GID_CONSUMER_TRANSACTION_TEST";
    public static final String PRODUCER_GROUP_ID = "GID_PRODUCER_TEST";
    public static final String PRODUCER_BROADCAST_GROUP_ID = "GID_PRODUCER_BROADCAST_TEST";
    public static final String PRODUCER_ORDER_GROUP_ID = "GID_PRODUCER_ORDER_TEST";
    public static final String PRODUCER_TRANSACTION_GROUP_ID = "GID_PRODUCER_TRANSACTION_TEST";
    public static final String TAG = "mq_test_tag";

    /**
     * NAMESRV_ADDR, 请在mq控制台 https://ons.console.aliyun.com 通过"实例管理--获取接入点信息--TCP协议接入点"获取
     */
    public static final String NAMESRV_ADDR = "localhost:9876";   // 此处没有协议

    /**
     * 启动namesrv服务：nohup sh bin/mqnamesrv &
     * 日志目录：{user.dir}/logs/rocketmqlogs/namesrv.log
     *
     * 启动broker服务：nohup sh bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true traceTopicEnable=true &  // autoCreateTopicEnable:自动创建topic  traceTopicEnable:消息链路跟踪
     * 日志目录：{user.dir}/logs/rocketmqlogs/broker.log
     *
     * 在bin目录下执行命令sh bin/mqadmin clusterList -n localhost:9876 // 查看broker是否连接到了nameServ
     * jps 命令查看nameSrv和broker是否启动
     *
     * 手动创建broker命令：sh bin/mqadmin updateTopic -n localhost:9876 -b localhost:10911 -t topic-test  // topic-test为主题名称
     *
     * 关闭namesrv服务：sh bin/mqshutdown namesrv
     * 关闭broker服务 ：sh bin/mqshutdown broker
     *
     */

}

package com.seiya.rocketmq.openservices.tcp.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.seiya.rocketmq.openservices.tcp.MqConfig;

import java.util.Date;
import java.util.Properties;

/**
 * MQ 发送事务消息示例 Demo
 */
public class TransactionMsgProducer {

    public static void main(String[] args) {
        Properties tranProducerProperties = new Properties();
        tranProducerProperties.setProperty(PropertyKeyConst.GROUP_ID, MqConfig.GROUP_ID);
        tranProducerProperties.setProperty(PropertyKeyConst.AccessKey, MqConfig.ACCESS_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.SecretKey, MqConfig.SECRET_KEY);
        tranProducerProperties.setProperty(PropertyKeyConst.NAMESRV_ADDR, MqConfig.NAMESRV_ADDR);
        // 初始化事务消息Producer时,需要注册一个本地事务状态的的Checker
        LocalTransactionCheckerImpl localTransactionChecker = new LocalTransactionCheckerImpl();
        // 创建事务消息生产者
        TransactionProducer transactionProducer = ONSFactory.createTransactionProducer(tranProducerProperties, localTransactionChecker);
        transactionProducer.start();

        // 创建消息
        Message message = new Message(MqConfig.TRANSACTION_TOPIC, MqConfig.TAG, "mq send transaction message test".getBytes());

        // 循环发送消息
        for (int i = 0; i < 1; i++) {
            try{
                SendResult sendResult = transactionProducer.send(message, new LocalTransactionExecuter() {
                    @Override
                    public TransactionStatus execute(Message msg, Object arg) {
                        System.out.println("执行本地事务, 并根据本地事务的状态提交TransactionStatus.");
                        return TransactionStatus.CommitTransaction;
                    }
                }, null);
                assert sendResult != null;
                System.out.println(new Date() + " Send mq message success! Topic is:" + MqConfig.TOPIC + " msgId is: " + sendResult.getMessageId());
            }catch (ONSClientException e){
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                System.out.println(new Date() + " Send mq message failed! Topic is:" + MqConfig.TOPIC);
                e.printStackTrace();
            }
        }

        System.out.println("Send transaction message success.");
    }

    /**
     * MQ 发送事务消息本地Check接口实现类
     */
    public static class LocalTransactionCheckerImpl implements LocalTransactionChecker {
        /**
         * 本地事务Checker,详见: https://help.aliyun.com/document_detail/29548.html?spm=5176.doc35104.6.133.pJkthu
         */
        @Override
        public TransactionStatus check(Message msg) {
            System.out.println("收到事务消息的回查请求, MsgId: " + msg.getMsgID());
            // CommitTransaction  RollbackTransaction  Unknow
            return TransactionStatus.CommitTransaction;
        }
    }
}

package org.example.mq.rocket.provider;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author Huang Z.Y.
 */
public class RocketProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("rocket");
        // 指定 NameServer 地址
        producer.setNamesrvAddr("127.0.0.1:9876");
        // 防止超时
        producer.setSendMsgTimeout(60000);

        // 启动生产者
        producer.start();
        // 创建一条消息
        Message msg = new Message("topic", "tag", "key", "hello".getBytes());
        // 发送消息并得到发送消息的结果
        SendResult response = producer.send(msg);
        System.out.println(response);

        // 关闭生产者
        producer.shutdown();
    }
}




package org.example.mq.rocket.provider;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author Huang Z.Y.
 */
public class AsynMessProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 创建生产者实例
        DefaultMQProducer producer = new DefaultMQProducer("AsyncProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        try {
            for (int i = 0; i < 10; i++) {
                Message msg = new Message("TopicAsync", "TagB", "OrderID_" + i, ("Hello RocketMQ " + i).getBytes());
                // 异步发送消息
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.printf("Async send success: %s%n", sendResult);
                    }

                    @Override
                    public void onException(Throwable e) {
                        System.err.printf("Async send failed: %s%n", e);
                    }
                });
            }
        } finally {
            producer.shutdown();
        }
    }
}

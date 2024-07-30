package org.example.mq.rocket.provider;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author Huang Z.Y.
 */
public class OrderedMessProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("OrderProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        for (int i = 0; i < 100; i++) {
            final int orderId = i % 10;
            Message msg = new Message("OrderTopic", "TagA", "OrderID_" + orderId, ("Hello RocketMQ " + i).getBytes());

            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                // 自定义路由规则，将消息发送到特定队列
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("Send result: %s%n", sendResult);
        }

        producer.shutdown();
    }
}

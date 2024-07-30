package org.example.mq.rocket.springboot;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Huang Z.Y.
 */

@RocketMQMessageListener(topic = "sendMessage_topic",
        consumerGroup = "consumer-group-test1",
        selectorExpression = "tagA || tagB",
        messageModel = MessageModel.CLUSTERING)
@Component
public class SelectorConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("Receive message1:" + s);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("处理完成");
    }
}


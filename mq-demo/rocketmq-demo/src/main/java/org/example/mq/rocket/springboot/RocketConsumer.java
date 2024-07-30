package org.example.mq.rocket.springboot;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Huang Z.Y.
 */
@Component
@RocketMQMessageListener(topic = "topic", consumerGroup = "springboot_consumer_group")
public class RocketConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }
}

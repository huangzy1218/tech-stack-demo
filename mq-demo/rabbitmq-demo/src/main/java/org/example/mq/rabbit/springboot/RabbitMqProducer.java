package org.example.mq.rabbit.springboot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@Component
public class RabbitMqProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String routingKey, String message) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE_NAME, routingKey, message);
        System.out.println(" [x] Sent '" + message + "' with routing key '" + routingKey + "'");
    }
}

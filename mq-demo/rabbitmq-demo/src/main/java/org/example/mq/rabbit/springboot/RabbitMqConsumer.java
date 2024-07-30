package org.example.mq.rabbit.springboot;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Huang Z.Y.
 */
@Component
public class RabbitMqConsumer {
    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println(" [x] Received '" + message + "'");
    }
}

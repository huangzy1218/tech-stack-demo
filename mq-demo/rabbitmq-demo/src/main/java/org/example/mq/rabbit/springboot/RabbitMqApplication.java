package org.example.mq.rabbit.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author Huang Z.Y.
 */
@SpringBootApplication
public class RabbitMqApplication implements CommandLineRunner {
    @Resource
    private RabbitMqProducer producer;

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        producer.sendMessage("quick.orange.rabbit", "Message for queue1 and queue2");
        producer.sendMessage("lazy.orange.elephant", "Message for queue1 and queue2");
        producer.sendMessage("quick.orange.fox", "Message for queue1 only");
        producer.sendMessage("lazy.brown.fox", "Message for queue2 only");
        producer.sendMessage("lazy.pink.rabbit", "Message for queue2 only");
        producer.sendMessage("quick.brown.fox", "Message for no queue");
        producer.sendMessage("quick.orange.male.rabbit", "Message for no queue");
        producer.sendMessage("lazy.orange.male.rabbit", "Message for queue2 only");
    }
}

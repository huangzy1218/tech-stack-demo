package org.example.mq.kafka.springboot;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author Huang Z.Y.
 */
@Component
public class KafkaReceiver {

    @KafkaListener(topics = "Topic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        System.out.println("Received message: " + record.value());
        // 手动提交偏移量
        acknowledgment.acknowledge();
    }
}

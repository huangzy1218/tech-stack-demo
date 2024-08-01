package org.example.mq.kafka.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Huang Z.Y.
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;


    public void send() {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("Topic", "message");
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + "your-message" +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + "your-message" + "] due to : " + ex.getMessage());
            }
        });
    }
}

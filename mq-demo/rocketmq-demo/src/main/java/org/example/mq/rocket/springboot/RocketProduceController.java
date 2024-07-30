package org.example.mq.rocket.springboot;

import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang Z.Y.
 */
@RestController
@AllArgsConstructor
public class RocketProduceController {
    private RocketMQTemplate rocketmqTemplate;

    @GetMapping("/origin")
    public String test() {
        Message<String> msg = MessageBuilder.withPayload("Hello RocketMQ")
                .setHeader("key", "value").build();
        rocketmqTemplate.send("topic", msg);
        return "Success";
    }

    @RequestMapping(value = "/rocket", method = RequestMethod.GET)
    public void noTag() {
        // convertAndSend() 发送普通字符串消息
        rocketmqTemplate.convertAndSend("sendMessage_topic", "Hello Word");
    }

    @RequestMapping(value = "/tagA", method = RequestMethod.GET)
    public void tagA() {
        rocketmqTemplate.convertAndSend("sendMessage_topic:tagA", "Hello world tagA");
    }

    @RequestMapping(value = "/tagB", method = RequestMethod.GET)
    public void tagB() {
        rocketmqTemplate.convertAndSend("sendMessage_topic:tagB", "Hello world tagB");
    }

    @RequestMapping(value = "/syncSend", method = RequestMethod.GET)
    public void syncSend() {
        String json = "发送同步消息";
        SendResult sendResult = rocketmqTemplate.syncSend("sendMessage_topic:1", json);
        System.out.println(sendResult);
    }

    @RequestMapping(value = "/aSyncSend", method = RequestMethod.GET)
    public void aSyncSend() {
        String json = "发送异步消息";
        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送消息成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送消息失败");
            }
        };
        rocketmqTemplate.asyncSend("sendMessage_topic", json, callback);
    }

    @RequestMapping(value = "/sendOneWay", method = RequestMethod.GET)
    public void sendOneWay() {
        rocketmqTemplate.sendOneWay("sendMessage_topic", "发送单向消息");
    }
}

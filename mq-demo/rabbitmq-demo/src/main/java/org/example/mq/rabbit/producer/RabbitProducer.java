package org.example.mq.rabbit.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Huang Z.Y.
 */
public class RabbitProducer {
    private final static String QUEUE_NAME = "demo";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂对象
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 服务器的主机名（在这里是本地主机）
        factory.setHost("localhost");

        // 使用 try-with-resources 语法，自动管理资源（连接和通道）
        // 创建与 RabbitMQ 服务器的连接
        try (Connection connection = factory.newConnection();
             // 创建一个通信通道
             Channel channel = connection.createChannel()) {
            // 声明一个队列，如果队列已经存在则不会重新创建

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 要发送的消息
            String message = "Hello World!";
            // 发送消息到指定队列
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 打印发送消息的确认信息
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(10000);
        } catch (Exception e) {
            // 捕获和打印任何可能发生的异常
            e.printStackTrace();
        }
    }
}

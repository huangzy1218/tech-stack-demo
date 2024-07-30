package org.example.mq.rabbit.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Huang Z.Y.
 */
public class RabbitConsumer {
    private final static String QUEUE_NAME = "demo";

    public static void main(String[] argv) throws Exception {
        // 创建一个连接工厂对象
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 服务器的主机名（在这里是本地主机）
        factory.setHost("localhost");

        // 使用 try-with-resources 语法，自动管理资源（连接和通道）
        try (Connection connection = factory.newConnection(); // 创建与 RabbitMQ 服务器的连接
             Channel channel = connection.createChannel()) { // 创建一个通信通道

            // 声明一个队列。如果队列不存在则创建它
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 打印等待消息的提示信息
            System.out.println(" [*] Waiting for messages. To exit press Ctrl+C");
            // 定义消息到达时的回调函数
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                // 将接收到的消息的字节数组转换为字符串
                String message = new String(delivery.getBody(), "UTF-8");
                // 打印接收到的消息
                System.out.println(" [x] Received '" + message + "'");
            };

            // 订阅队列，接收消息
            // 参数含义：
            // QUEUE_NAME: 消息接收的队列名称
            // true: 自动确认消息（默认设置为true，表示消息被接收后会被自动确认）
            // deliverCallback: 消息到达时的回调函数
            // consumerTag -> {}: 处理消费者标签的回调函数（此处为空操作）
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            // 捕获并打印任何可能发生的异常
            e.printStackTrace();
        }
    }
}

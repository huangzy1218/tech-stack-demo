package org.example.mq.kafka.producer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Huang Z.Y.
 */
public class KafkaMqProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String server = "localhost:9092";
        // 1.创建kafka生产者的配置信息
        Properties properties = new Properties();
        // 2.指定连接的Kafka集群
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        // 3.ACK应答级别
        // properties.put("acks", "all");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 4.重试次数
        properties.put("retries", 0);
        // 5.批次大小
        properties.put("batch.size", 16384);
        // 6.等待时间
        properties.put("linger.ms", 10000);
        // 7.RecordAccumulator 缓冲区大小
        properties.put("buffer.memory", 33554432);
        // 8.key,value的序列化
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 9.创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        // 10.发送数据

        // 异步
        // 生产者写一条消息的时候，先是写到某个缓冲区，
        // 这个缓冲区里的数据还没写到 broker 集群里的某个分区的时候，
        // 它就返回到 client去。虽然效率快，但是不能保证消息一定被发送
        producer.send(new ProducerRecord<>("test2", "fmy", "这是生产者异步发送的消息!"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.err.println("发送消息失败：" +
                            exception.getStackTrace());
                }
                if (metadata != null) {
                    System.out.println("异步方式发送消息结果：" + "topic-" + metadata.topic() + "|partition-" + metadata.partition() + "|offset-" + metadata.offset());
                }
            }
        });

        // 同步
        // 生产者写一条消息的时候，它就立马发送到某个分区去。
        // follower 还需要从 leader 拉取消息到本地，follower 再向 leader 发送确认，
        // leader 再向客户端发送确认。由于这一套流程之后，客户端才能得到确认，速度慢
        Future<RecordMetadata> demo = producer.send(new ProducerRecord<>("demo", "neu", "这里是生产者同步发送的消息"));
        RecordMetadata recordMetadata = demo.get();
        System.out.println("得到ack");
        // 11. 关闭资源
        producer.close();
    }
}

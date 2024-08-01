package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author Huang Z.Y.
 */
public class RedissonApplication {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://39.107.235.5:6379")
                .setDatabase(0);
        // 获取客户端
        RedissonClient redissonClient = Redisson.create(config);
        // 获取所有的key
        redissonClient.getKeys().getKeys().forEach(System.out::println);
        // 关闭客户端
        redissonClient.shutdown();
    }
}

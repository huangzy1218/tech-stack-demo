package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class UniqueIdApplication {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://39.107.235.5:6379")
                .setDatabase(0);
        // 获取客户端
        RedissonClient redissonClient = Redisson.create(config);
        final String lockKey = "lock";
        RAtomicLong atomicLong = redissonClient.getAtomicLong(lockKey);
        atomicLong.expire(30, TimeUnit.SECONDS);
        System.out.println("ID: " + atomicLong.incrementAndGet());
        redissonClient.shutdown();
    }
}

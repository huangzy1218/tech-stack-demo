package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class RedissonLockApplication {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://39.107.235.5:6379")
                .setDatabase(0);
        // 获取客户端
        RedissonClient redissonClient = Redisson.create(config);
        // 获取锁对象实例
        RLock lock = redissonClient.getLock("redisson-lock");
        try {
            // 尝试 5s 内获取锁，若获取到，最长 60s 自动释放
            boolean res = lock.tryLock(5, 60, TimeUnit.SECONDS);
            if (res) {
                System.out.println("获取锁成功");
            }
        } catch (Exception e) {
            System.out.println("获取锁失败: " + e.getMessage());
        } finally {
            // 最后释放锁
            lock.unlock();
        }

        redissonClient.shutdown();
    }
}

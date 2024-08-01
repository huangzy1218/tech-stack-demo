package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class RedissonRedLockApplication {
    public static void main(String[] args) {
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://192.168.3.111:6379").setPassword("a123456").setDatabase(0);
        RedissonClient redissonClient1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSingleServer().setAddress("redis://192.168.3.112:6379").setPassword("a123456").setDatabase(0);
        RedissonClient redissonClient2 = Redisson.create(config2);

        Config config3 = new Config();
        config3.useSingleServer().setAddress("redis://192.168.3.113:6379").setPassword("a123456").setDatabase(0);
        RedissonClient redissonClient3 = Redisson.create(config3);

        // 获取多个 RLock 对象
        final String lockKey = "abc";
        RLock lock1 = redissonClient1.getLock(lockKey);
        RLock lock2 = redissonClient2.getLock(lockKey);
        RLock lock3 = redissonClient3.getLock(lockKey);

        // 根据多个 RLock 对象构建 RedissonRedLock （最核心的差别就在这里）
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);

        try {
            // 尝试5秒内获取锁，如果获取到了，最长60秒自动释放
            boolean res = redLock.tryLock(5L, 60L, TimeUnit.SECONDS);
            if (res) {
                // 成功获得锁，在这里处理业务
                System.out.println("获取锁成功");

            }
        } catch (Exception e) {
            System.out.println("获取锁失败，失败原因：" + e.getMessage());
        } finally {
            // 无论如何, 最后都要解锁
            redLock.unlock();
        }
    }
}

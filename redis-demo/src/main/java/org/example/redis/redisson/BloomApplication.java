package org.example.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class BloomApplication {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://39.107.235.5:6379")
                .setDatabase(0);
        // 获取客户端
        RedissonClient redissonClient = Redisson.create(config);
        RBloomFilter<Object> filter = redissonClient.getBloomFilter("seqId");
        // 预期插入数据量和期望误差率
        filter.tryInit(10000, 0.01);
        filter.add("1000");
        filter.add("2000");
        filter.add("3000");
        // 设置过期时间
        filter.expire(30, TimeUnit.SECONDS);
        // 判断是否存在
        System.out.println("3000: " + filter.contains("3000"));
        System.out.println("2000: " + filter.contains("2000"));
        System.out.println("9999: " + filter.contains("9999"));

        redissonClient.shutdown();
    }
}

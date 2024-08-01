package org.example.redis.redisson;

import org.example.entity.Student;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class RedissonOpApplication {
    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://39.107.235.5:6379")
                .setDatabase(0);
        // 获取客户端
        RedissonClient redissonClient = Redisson.create(config);

        // 字符串操作
        RBucket<Object> strKey = redissonClient.getBucket("name");
        strKey.set("Huang Z.Y.", 30, TimeUnit.HOURS);
        System.out.println("String: " + redissonClient.getBucket("name").get());

        // 对象操作
        Student student = new Student();
        student.setName("Huang Z.Y.");
        student.setAge(21);
        student.setId(1000L);
        RBucket<Student> studentBucket = redissonClient.getBucket("student");
        studentBucket.set(student, 30, TimeUnit.SECONDS);
        System.out.println("Object: " + redissonClient.getBucket("student").get());

        // 哈希操作
        RMap<String, String> map = redissonClient.getMap("map");
        map.put("name", "Huang Z.Y.");
        map.put("age", "21");
        map.put("id", "1000");
        map.expire(30, TimeUnit.SECONDS);
        System.out.println("Map: " + redissonClient.getMap("map").get("name"));

        // 列表操作
        RList<Integer> list = redissonClient.getList("list");
        list.add(1);
        list.add(2);
        list.add(3);
        list.expire(30, TimeUnit.SECONDS);
        System.out.println("List: " + redissonClient.getList("list"));

        // 集合操作
        RSet<Integer> set = redissonClient.getSet("set");
        set.add(1);
        set.add(2);
        set.add(1);
        set.expire(30, TimeUnit.SECONDS);
        System.out.println("Set: " + redissonClient.getSet("set"));

        // 有序集合
        RSortedSet<Student> sortSet = redissonClient.getSortedSet("sortSet");
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("张三");
        student1.setAge(18);
        sortSet.add(student1);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("李四");
        student2.setAge(19);
        sortSet.add(student2);

        // 通过 key 获取 value
        System.out.println(redissonClient.getSortedSet("sortSet"));
        redissonClient.shutdown();
    }
}

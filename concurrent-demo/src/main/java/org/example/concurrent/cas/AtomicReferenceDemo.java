package org.example.concurrent.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Huang Z.Y.
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        // 初始用户对象
        User initialUser = new User("Alice", 30);

        // 使用 AtomicReference 封装用户对象
        AtomicReference<User> atomicUser = new AtomicReference<>(initialUser);

        // 打印初始值
        System.out.println("Initial user: " + atomicUser.get());

        // 创建一个新的用户对象
        User newUser = new User("Bob", 25);

        // 尝试使用 compareAndSet 方法原子性地更新用户对象
        if (atomicUser.compareAndSet(initialUser, newUser)) {
            System.out.println("User updated successfully.");
        } else {
            System.out.println("Failed to update user.");
        }

        // 打印更新后的值
        System.out.println("Updated user: " + atomicUser.get());
    }

    static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}

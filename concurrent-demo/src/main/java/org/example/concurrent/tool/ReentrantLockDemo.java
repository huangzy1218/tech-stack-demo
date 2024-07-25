package org.example.concurrent.tool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Huang Z.Y.
 */
public class ReentrantLockDemo {
    // 公平锁
    private final Lock fairLock = new ReentrantLock(true);
    // 非公平锁
    private final Lock nonFairLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo example = new ReentrantLockDemo();

        // 公平锁示例
        Thread fairLockThread = new Thread(() -> example.performTaskWithFairLock());
        Thread fairLockInterruptibleThread = new Thread(() -> {
            try {
                example.performTaskWithInterruptibleLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        fairLockThread.start();
        fairLockInterruptibleThread.start();

        fairLockThread.join();
        fairLockInterruptibleThread.join();

        // 非公平锁示例
        Thread nonFairLockThread1 = new Thread(() -> example.performTaskWithNonFairLock());
        Thread nonFairLockThread2 = new Thread(() -> example.performTaskWithNonFairLock());
        nonFairLockThread1.start();
        nonFairLockThread2.start();

        nonFairLockThread1.join();
        nonFairLockThread2.join();

        // 限时请求获取锁示例
        Thread timeoutLockThread = new Thread(() -> {
            try {
                boolean acquired = example.tryToPerformTaskWithTimeoutLock(3, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " tried to acquire fairLock with timeout: " + acquired);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timeoutLockThread.start();
        timeoutLockThread.join();
    }

    // 使用公平锁的方法示例
    public void performTaskWithFairLock() {
        try {
            // 获取公平锁
            fairLock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired fairLock");
            // 模拟执行任务
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " releasing fairLock");
            // 释放公平锁
            fairLock.unlock();
        }
    }

    // 使用非公平锁的方法示例
    public void performTaskWithNonFairLock() {
        try {
            // 获取非公平锁
            nonFairLock.lock();
            System.out.println(Thread.currentThread().getName() + " acquired nonFairLock");
            // 模拟执行任务，但这里没有休眠，快速释放锁
        } finally {
            System.out.println(Thread.currentThread().getName() + " releasing nonFairLock");
            nonFairLock.unlock();
        }
    }

    // 优先响应中断的方法示例
    public void performTaskWithInterruptibleLock() throws InterruptedException {
        try {
            // 响应中断地获取公平锁
            fairLock.lockInterruptibly();
            System.out.println(Thread.currentThread().getName() + " acquired fairLock interruptibly");
            Thread.sleep(2000);
        } finally {
            System.out.println(Thread.currentThread().getName() + " releasing fairLock interruptibly");
            fairLock.unlock();
        }
    }

    // 限时请求获取锁的方法示例
    public boolean tryToPerformTaskWithTimeoutLock(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            // 限时请求获取公平锁
            return fairLock.tryLock(timeout, unit);
        } finally {
            System.out.println(Thread.currentThread().getName() + " releasing fairLock with timeout");
            fairLock.unlock();
        }
    }
}

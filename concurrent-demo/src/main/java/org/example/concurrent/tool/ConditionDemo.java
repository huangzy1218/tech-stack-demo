package org.example.concurrent.tool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Huang Z.Y.
 */
public class ConditionDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private final Queue<Integer> queue = new LinkedList<>();
    // 生产者消费者队列的最大容量
    private final int capacity = 5;

    public static void main(String[] args) {
        ConditionDemo demo = new ConditionDemo();

        // Producer thread
        Thread producerThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    demo.produce(i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consumer thread
        Thread consumerThread = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    demo.consume();
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producerThread.start();
        consumerThread.start();

        // 等待两个线程执行完毕
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All tasks finished");
    }

    public void produce(int num) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                System.out.println("Queue is full, Producer is waiting...");
                notFull.await();
            }
            queue.offer(num);
            System.out.println("Produced: " + num);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                System.out.println("Queue is empty, Consumer is waiting...");
                notEmpty.await();
            }
            int num = queue.poll();
            System.out.println("Consumed: " + num);
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}

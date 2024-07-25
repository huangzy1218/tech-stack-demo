package org.example.concurrent.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class NamedTreadPoolDemo {
    static class TargetTask implements Runnable {
        static AtomicInteger taskNo = new AtomicInteger(1);
        String taskName;

        public TargetTask() {
            taskName = "Task-" + taskNo;
            taskNo.incrementAndGet();
        }

        @Override
        public void run() {
            log.info("{}: {} 正在运行", Thread.currentThread().getName(), taskName);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println(taskName + " 结束运行");
        }
    }

    static class SimpleThreadFactory implements ThreadFactory {
        static AtomicInteger threadNo = new AtomicInteger(1);

        public static void main(String[] args) throws InterruptedException {
            ExecutorService pool = Executors.newFixedThreadPool(2, new SimpleThreadFactory());
            // ExecutorService pool=Executors.newFixedThreadPool(2);
            for (int i = 0; i < 5; i++) {
                pool.submit(new TargetTask());
            }

            Thread.sleep(5000);
            pool.shutdown();
        }

        @Override
        public Thread newThread(Runnable task) {
            String threadName = "simpleThread-" + threadNo;
            log.info("创建线程：{}", threadName);
            threadNo.incrementAndGet();
            Thread thread = new Thread(task, threadName);
            thread.setDaemon(true);
            return thread;
        }
    }
}

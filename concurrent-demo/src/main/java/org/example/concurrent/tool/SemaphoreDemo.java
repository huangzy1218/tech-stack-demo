package org.example.concurrent.tool;

import java.util.concurrent.Semaphore;

/**
 * @author Huang Z.Y.
 */
public class SemaphoreDemo {
    private static final int NUM_THREADS = 10;
    // 信号量，允许同时执行的线程数为 3
    private static final Semaphore semaphore = new Semaphore(3);

    public static void main(String[] args) {
        for (int i = 1; i <= NUM_THREADS; i++) {
            Thread thread = new Thread(new Task(i));
            thread.start();
        }
    }

    static class Task implements Runnable {
        private final int id;

        Task(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // 获取信号量，如果没有可用的许可证，则阻塞直到获取到许可证为止
                // 一次可获取多个许可证
                semaphore.acquire();
                System.out.println("Thread " + id + " is working");
                Thread.sleep(2000);
                System.out.println("Thread " + id + " finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}

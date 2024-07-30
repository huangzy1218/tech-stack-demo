package org.example.concurrent.tool;

import java.util.concurrent.CountDownLatch;

/**
 * @author Huang Z.Y.
 */
public class CountDownLatchDemo {
    private static final int NUM_THREADS = 3;

    public static void main(String[] args) {
        // 线程可以通过等待之前必须调用 countDown 的次数
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);

        // 创建并启动多个工作线程
        for (int i = 1; i <= NUM_THREADS; i++) {
            Thread workerThread = new Thread(new WorkerTask(i, latch));
            workerThread.start();
        }

        try {
            // 等待所有工作线程完成
            latch.await();
            System.out.println("All threads have finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class WorkerTask implements Runnable {
        private final int id;
        private final CountDownLatch latch;

        WorkerTask(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println("Worker " + id + " is working");
                Thread.sleep(2000); // 模拟工作耗时
                System.out.println("Worker " + id + " finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 完成工作，计数器减一
                latch.countDown();
            }
        }
    }

}

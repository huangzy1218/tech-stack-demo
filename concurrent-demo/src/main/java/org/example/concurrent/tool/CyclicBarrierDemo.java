package org.example.concurrent.tool;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Huang Z.Y.
 */
public class CyclicBarrierDemo {
    private static final int NUM_THREADS = 3;

    public static void main(String[] args) {
        Runnable barrierAction = () -> System.out.println("Barrier action executed");

        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, barrierAction);

        // 创建并启动多个工作线程
        for (int i = 1; i <= NUM_THREADS; i++) {
            Thread workerThread = new Thread(new WorkerTask(i, barrier));
            workerThread.start();
        }
    }

    static class WorkerTask implements Runnable {
        private final int id;
        private final CyclicBarrier barrier;

        WorkerTask(int id, CyclicBarrier barrier) {
            this.id = id;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println("Worker " + id + " is working");
                Thread.sleep(2000);
                System.out.println("Worker " + id + " is waiting at the barrier");
                // 等待所有线程到达栅栏
                barrier.await();

                System.out.println("Worker " + id + " finished");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}

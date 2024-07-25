package org.example.concurrent.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Huang Z.Y.
 */
public class ThreadPoolExecutorDemo {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        // 使用阿里巴巴推荐的创建线程池的方式
        // 通过 ThreadPoolExecutor 构造函数自定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10; i++) {
            // 创建 WorkerThread 对象（WorkerThread类实现了 Runnable 接口）
            Runnable worker = new DemoRunnable("" + i);
            // 执行Runnable
            executor.execute(worker);
        }
        // 终止线程池，继续执行未完成的任务
        executor.shutdown();
        // 立即终止线程池
        // executor.shutdownNow();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}


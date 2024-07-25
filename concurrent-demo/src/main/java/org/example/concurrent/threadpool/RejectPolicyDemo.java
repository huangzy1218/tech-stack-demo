package org.example.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * @author Huang Z.Y.
 */
public class RejectPolicyDemo {
    public static void main(String[] args) throws InterruptedException {
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAlive = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        RejectedExecutionHandler policy = new CustomIgnorePolicy();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAlive, unit, workQueue, policy);

        pool.prestartAllCoreThreads();
        for (int i = 0; i < 11; i++) {
            pool.execute(new DemoRunnable("Hello World " + i));
        }
        Thread.sleep(5000);
        pool.shutdown();

    }
}

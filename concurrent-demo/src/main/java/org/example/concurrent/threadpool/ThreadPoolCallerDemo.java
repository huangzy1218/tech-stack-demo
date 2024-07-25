package org.example.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * @author Huang Z.Y.
 */
public class ThreadPoolCallerDemo {
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 123;
            }
        });
        try {
            Integer result = future.get();
            System.out.println("result:" + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Thread.sleep(1000);
        pool.shutdown();
    }
}

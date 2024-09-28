package org.example.concurrent.threadpool.custom;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Huang Z.Y.
 */
public class SimpleThreadPool {

    private final BlockingQueue<Runnable> taskQueue;
    private final WorkerThread[] workerThreads;
    private final int corePoolSize;
    private final int maxPoolSize;
    private final AtomicInteger currentPoolSize = new AtomicInteger(0);
    private volatile boolean isShutdown = false;

    public SimpleThreadPool(int corePoolSize, int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workerThreads = new WorkerThread[corePoolSize];

        for (int i = 0; i < corePoolSize; i++) {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start();
        }
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool is shut down");
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        synchronized (this) {
            taskQueue.offer(futureTask);
            if (currentPoolSize.get() < corePoolSize || (currentPoolSize.get() < maxPoolSize && taskQueue.size() > corePoolSize)) {
                WorkerThread newWorker = new WorkerThread();
                newWorker.start();
                currentPoolSize.incrementAndGet();
            }
        }
        return futureTask;
    }

    public void submit(Runnable task) {
        submit(Executors.callable(task));
    }

    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workerThreads) {
            worker.interrupt();
        }
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    if (isShutdown) {
                        break;
                    }
                }
            }
            currentPoolSize.decrementAndGet();
        }
    }

    public static void main(String[] args) {
        SimpleThreadPool threadPool = new SimpleThreadPool(3, 10);

        // Submit tasks with return values
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            Future<Integer> future = threadPool.submit(() -> {
                System.out.println("Executing task " + taskId + " by " + Thread.currentThread().getName());
                Thread.sleep(1000); // Simulate work
                return taskId * 2; // Return some value
            });

            // Handle the result
            try {
                System.out.println("Result of task " + taskId + ": " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Submit tasks without return values
        for (int i = 10; i < 15; i++) {
            final int taskId = i;
            threadPool.submit(() -> {
                System.out.println("Executing task " + taskId + " by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        threadPool.shutdown();
    }

}
    
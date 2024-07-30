package org.example.concurrent.tool;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Huang Z.Y.
 */
public class ReadWriteLockDemo {
    private static final int NUM_READERS = 3;
    private static final int NUM_WRITERS = 2;

    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // 共享资源
    private static String sharedResource = "Initial Value";

    public static void main(String[] args) {
        // 创建读线程
        for (int i = 1; i <= NUM_READERS; i++) {
            Thread readerThread = new Thread(new ReaderTask(i));
            readerThread.start();
        }

        // 创建写线程
        for (int i = 1; i <= NUM_WRITERS; i++) {
            Thread writerThread = new Thread(new WriterTask(i));
            writerThread.start();
        }
    }

    // 读线程任务
    static class ReaderTask implements Runnable {
        private final int id;

        ReaderTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                // 获取读锁
                readWriteLock.readLock().lock();
                try {
                    System.out.println("Reader " + id + " reads: " + sharedResource);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    readWriteLock.readLock().unlock();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 写线程任务
    static class WriterTask implements Runnable {
        private final int id;

        WriterTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                readWriteLock.writeLock().lock();
                try {
                    String newValue = "New Value written by Writer " + id;
                    sharedResource = newValue;
                    System.out.println("Writer " + id + " writes: " + newValue);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    readWriteLock.writeLock().unlock();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

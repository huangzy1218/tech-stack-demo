package org.example.concurrent.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Huang Z.Y.
 */
@Slf4j
public class CustomIgnorePolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("{} rejected, task counts {}", Thread.currentThread().getName(), executor.getTaskCount());
    }
}

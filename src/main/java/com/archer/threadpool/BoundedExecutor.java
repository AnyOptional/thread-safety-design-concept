package com.archer.threadpool;

import com.archer.annotation.ThreadSafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class BoundedExecutor {
    /**
     * ThreadPoolExecutor并没有预留的饱和策略来进行阻塞，
     * 使用Semaphore可以做到这一点。
     *
     * Semaphore可以限制任务注入率，当然BlockingQueue就
     * 没必要限制大小了。设置Semaphore的大小等于池的大小和
     * 允许排队的任务的数量。
     */

    private final Semaphore semaphore;

    private final ExecutorService executor;

    public BoundedExecutor(ExecutorService executor, int boundedSize) {
        this.executor = executor;
        this.semaphore = new Semaphore(boundedSize);
    }

    public void submit(Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }
}

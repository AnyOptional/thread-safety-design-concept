package com.archer.synchronizer;

import com.archer.annotation.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用锁实现信号量
 */
@ThreadSafe
public class SemaphoreImpl {

    private final Lock lock = new ReentrantLock();
    private final Condition permitsCondition = lock.newCondition();

    private int permits;
    private final int originalPermits;

    SemaphoreImpl(int permits) {
        this.permits = permits;
        this.originalPermits = permits;
    }

    void accquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits <= 0) {
                permitsCondition.await();
            }
            --permits;
        } finally {
            lock.unlock();
        }
    }

    void release() {
        lock.lock();
        try {
            permitsCondition.signal();
            if (permits < originalPermits) {
                ++permits;
            }
        } finally {
            lock.unlock();
        }
    }
}

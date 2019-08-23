package com.archer.synchronizer;

import com.archer.annotation.ThreadSafe;

import java.util.concurrent.TimeUnit;

/**
 * 使用轮询+休眠实现阻塞(bad)
 *
 * put/take内部不要处理InterruptedException，
 * 把它留给上层调用者，因为在一个长时间的阻塞中进行
 * 打断是有意义的。
 */
@ThreadSafe
public class SleeepyBoundedBuffer<E> extends AbstractBoundedBuffer<E> {

    SleeepyBoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(E e) throws InterruptedException {
        while (isFull()) {
            TimeUnit.SECONDS.sleep(1);
        }
        doPut(e);
    }

    public synchronized E take() throws InterruptedException {
        while (isEmpty()) {
            TimeUnit.SECONDS.sleep(1);
        }
        return doTake();
    }
}

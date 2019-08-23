package com.archer.synchronizer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<E> extends AbstractBoundedBuffer<E>  {

    private final Lock lock = new ReentrantLock();
    private final Condition emptyCondition = lock.newCondition();
    private final Condition fullCondition = lock.newCondition();

    BoundedBuffer(int capacity) {
        super(capacity);
    }

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            /**
             * isFull()可能在其他线程调用signal时是真的，
             * 但是await从被唤醒到再次请求锁的这段时间可能又
             * 变成假的(可能有别的线程在这期间获得锁并改变了对象状态)，
             * 因此await唤醒后需要再次测试条件变量
             */
            while (isFull()) {
                fullCondition.await();
            }
            doPut(e);
            emptyCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            /**
             * 当使用条件等待时，object.wait condition.await
             * - 永远设置一个条件谓词——一些对象的状态测试，线程执行前必须满足它
             * - 永远在调用wait前测试条件谓词，并且在wait返回时再次测试
             * - 永远在循环中调用wait
             * - 确保构成条件谓词的状态变量被锁保护，而这个锁正是与条件队列相关联的
             * - 当调用wait/notify时要持有与条件队列相关联的锁，并且在检测条件谓词
             * 之后、开始执行被保护的逻辑之前，不要释放锁。
             */
            while (isEmpty()) {
                emptyCondition.await();
            }
            E e = doTake();
            fullCondition.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

}

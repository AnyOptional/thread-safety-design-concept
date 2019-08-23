package com.archer.synchronizer;

import com.archer.annotation.ThreadSafe;

@ThreadSafe
public abstract class AbstractBoundedBuffer<E> {

    private final E[] buf;
    private int tail, head, count;

    AbstractBoundedBuffer(int capacity) {
        this.buf = (E [])new Object[capacity];
    }

    public synchronized final boolean isFull() {
        return count == buf.length;
    }

    public synchronized final boolean isEmpty() {
        return count == 0;
    }

    protected synchronized final void doPut(E e) {
        buf[tail] = e;
        if (++tail == buf.length) {
            tail = 0;
        }
        ++count;
    }

    protected synchronized final E doTake() {
        E e = buf[head];
        buf[head] = null;
        if (++head == buf.length) {
            head =  0;
        }
        return e;
    }
}

package com.archer.synchronizer;

import com.archer.annotation.ThreadSafe;

import java.util.concurrent.TimeUnit;

@ThreadSafe
public class GrumpyBoundedBuffer<E> extends AbstractBoundedBuffer<E> {

    GrumpyBoundedBuffer(int capacity) {
        super((capacity));
    }

    static class BufferFullException extends IllegalStateException { }
    static class BufferEmptyException extends IllegalStateException { }


    public synchronized void put(E e) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(e);
    }

    public synchronized E take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }

    public static void main(String[] args) throws InterruptedException {

        /**
         * GrumpyBoundedBuffer在先验条件失败时抛出异常，
         * 外部调用时需要捕获异常并重试，不优雅。
         */
        GrumpyBoundedBuffer<String> buffer = new GrumpyBoundedBuffer<>(5);
        String item = null;
        while (true) {
            try {
                item = buffer.take();
                break;
            } catch (BufferEmptyException e) {
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

}

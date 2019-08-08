package com.archer.threadsafety;

import com.archer.annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadSafeCalculators {

    @ThreadSafe
    public class ThreadSafeCalculatorV1 {

        /**
         * 在一个stateless类中加入了一个状态变量，
         * 并且这个状态变量是被线程安全的类管理的，
         * 因而不会破坏线程安全性。
         *
         * 如果是假如多个线程安全的状态变量呢？还会维持
         * 整体的线程安全性吗？
         * - 答案是不一定。因为多个状态变量之间有可能不是互相独立的，
         * 如果存在依赖关系，就不能保证整体的线程安全性了。
         */
        private AtomicLong counter;

        public long getCounter() {
            return counter.get();
        }

        public int calculate(int x, int y) {
            /**
             * 将自增操作改为原子操作，
             * 这样每次操作都会得到预期的结果，
             * 即counter准确的加1.
             */
            counter.incrementAndGet();
            return x + y;
        }
    }


    @ThreadSafe
    public class ThreadSafeCalculatorV2 {

        private long counter;

        /**
         * 为了保证获取的counter不是来自线程缓存，
         * 读取操作也需要加锁
         */
        public synchronized long getCounter() {
            return counter;
        }

        public int calculate(int x, int y) {
            /**
             * 利用内置锁将++counter包装成原子操作
             */
            synchronized (this) {
                ++counter;
            }
            return x + y;
        }
    }
}

package com.archer.buildingblocks;

import com.archer.annotation.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

public class Memorizers {

    interface Computable<A, V> {
        V compute(A arg);
    }

    @ThreadSafe
    static class Memorizer1<A, V> implements Computable<A, V> {

        private final Map<A, V> cache = new HashMap<>();
        private final Computable<A, V> computable;

        public Memorizer1(Computable<A, V> computable) {
            this.computable = computable;
        }

        /**
         * HashMap不是线程安全的，为了保证两个线程不会同时访问cache，
         * 对整个compute方法加了锁。这保证了线程安全，却带来了一个明显
         * 的可伸缩性问题。
         *
         * 对compute方法的访问只能串行执行，所以有着糟糕的并发行为。
         */
        @Override
        public synchronized V compute(A arg) {
            V result = cache.get(arg);
            if (result == null) {
                result = computable.compute(arg);
                cache.put(arg, result);
            }
            return result;
        }
    }

    static class Memorizer2<A, V> implements Computable<A, V> {

        private final Map<A, V> cache = new HashMap<>();
        private final Computable<A, V> computable;

        public Memorizer2(Computable<A, V> computable) {
            this.computable = computable;
        }

        /**
         * 通过减小锁区提高了一点并发能力,
         * 这种加锁方式从线程安全的角度看就相当于
         * 把HashMap替换成ConcurrentHashMap。
         */
        @Override
        public V compute(A arg) {
            V result = null;
            synchronized (this) {
                result = cache.get(arg);
            }
            /**
             * 这里有一个漏洞。
             * 两个线程可能同时检测到result为空，从而往缓存里写了两次，
             * 然而如果对这个先检查后执行的情况加锁，那么就和Memorizer1一样了
             */
            if (result == null) {
                result = computable.compute(arg);
                synchronized (this) {
                    cache.put(arg, result);
                }
            }
            return result;
        }
    }

    @ThreadSafe
    static class Memorizer3<A, V> implements Computable<A, V> {

        private final Map<A, FutureTask<V>> cache = new ConcurrentHashMap<>();
        private final Computable<A, V> computable;

        public Memorizer3(Computable<A, V> computable) {
            this.computable = computable;
        }

        @Override
        public V compute(A arg) {
            while (true) {
                /**
                 * FutureTask代表了一个计算过程，可能已经结束也可能正在运行中。
                 * FutureTask只要结果可用，就会立刻将结果返回，否则它一直阻塞，
                 * 等待结果被计算出来，并返回。
                 */
                FutureTask<V> task = cache.get(arg);
                if (task == null) {
                    FutureTask<V> futureTask = new FutureTask<>(new Callable<V>() {
                        @Override
                        public V call() throws Exception {
                            return computable.compute(arg);
                        }
                    });
                    /**
                     * putIfAbsent避免写入两次
                     */
                    task = cache.putIfAbsent(arg, futureTask);
                    if (task == null) {
                        task = futureTask;
                        task.run();
                    }
                }
                try {
                    task.get();
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        }
    }

}

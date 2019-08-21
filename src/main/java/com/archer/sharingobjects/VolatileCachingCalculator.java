package com.archer.sharingobjects;

import com.archer.annotation.Immutable;
import com.archer.annotation.ThreadSafe;

/**
 * 通过不可变对象和volatile保证线程安全。
 *
 * 原理：与cache域相关的操作不会相互干扰，因为cache是不可变的，
 * 而且每次只有一条代码路径访问它。不可变的容器对象持有与不变性
 * 条件相关的多个变量，并使用volatile保证了及时的可见性。
 * 这两个前提保证了即使没有显示用到锁，仍然是线程安全的。
 */
@ThreadSafe
public class VolatileCachingCalculator {

    /**
     * 对x和y的缓存是一组相关操作，必须是原子性的。
     * 在这种情况下，可以考虑为它们创建不可变的容器类。
     *
     * 通过使用不可变变量来持有所有变量，可以消除在访问
     * 和更新这些变量时的竞争条件。
     */
    @Immutable
    static class ValueCache {
        private final int x;
        private final int y;

        ValueCache(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }

    /**
     * ValueCache是不可变的，一个线程持有了ValueCache对象的引用，永远
     * 不用担心其它线程更改它的状态。
     * 如果要更新它的状态，只需要创建一个新的ValueCache对象来替换就可以了。
     */
    private volatile ValueCache cache = new ValueCache(0, 0);

    public int calculate(int x, int y) {
        if (cache.getX() == x && cache.getY() == y) {
            return cache.getX() + cache.getY();
        }
        /**
         * 更新cache时，其它线程可能还需要使用cache的状态，此时仍可能出现数据不一致的时候。
         * 通过设置volatile修饰符，新数据会立即对所有线程可见，因为就不会读到过期值。
         */
        cache = new ValueCache(x, y);
        return x + y;
    }
}

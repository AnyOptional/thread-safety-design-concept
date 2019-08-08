package com.archer.threadsafety;

import com.archer.annotation.GuardedBy;
import com.archer.annotation.ThreadSafe;

/**
 *  SafeCachingCalculator的不变约束是缓存的x和y需要和calculate()方法参数里的
 *  x和y对应。
 */
@ThreadSafe
public class SafeCachingCalculator {

    @GuardedBy("this")
    private int x, y;

    /**
     * 保证了读取和更新x/y是原子操作，不会出现数据不一致的情况。
     */
    public int calculate(int x, int y) {

        /**
         * 没有必要整个方法都加锁，这会导致同一时刻只有一个线程能
         * 访问calculate方法，造成响应降低，并发性不高。
         */
        int cachedX = 0;
        int cachedY = 0;
        synchronized (this) {
            cachedX = this.x;
            cachedY = this.y;
        }
        /**
         * 假如这里是比较复杂的计算，减小锁区就提高了并发性
         */
        if (x == cachedX && y == cachedY) {
            return cachedX + cachedY;
        }

        synchronized (this) {
            this.x = x;
            this.y = y;
        }
        return x + y;
    }
}

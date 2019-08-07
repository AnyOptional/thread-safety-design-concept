package com.archer.threadsafety;

import com.archer.annotation.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@NotThreadSafe
public class UnsafeCachingCalculator {

    /**
     * 毫无意义的缓存，只是为了演示。
     *
     * 虽然x和y各自都是线程安全的，但是UnsafeCachingCalculator类存在竞争条件。
     * 缓存的x和y的更新和读取必须是原子操作，否则就会出现数据不一致的情况(使用了旧的x新的y之类的)。
     */
    private AtomicInteger x, y;

    public int calculate(int x, int y) {

        int cachedX = this.x.get();
        int cachedY = this.y.get();
        if (x == cachedX && y == cachedY) {
            return cachedX + cachedY;
        }

        this.x.lazySet(x);
        this.y.lazySet(y);
        return x + y;
    }
}

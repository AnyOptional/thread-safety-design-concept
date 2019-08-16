package com.archer.composingobjects;

import com.archer.annotation.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Range类的不变约束：lowerBound <= upperBound
 */
@NotThreadSafe
public class UnsafeRange {

    private AtomicInteger lowerBound;
    private AtomicInteger upperBound;

    /**
     * setLowerBound和setUpperBound都试图维护类的不变约束，
     * 然而这是徒劳的。虽然lowerBound和upperBound都是线程安全的，
     * 但是它们的值并不是独立的。
     *
     * 考虑lower = 0， upper = 10的情景，这是合法的。
     * 同一时刻，线程A设置lower = 5， 线程B设置upper = 4，
     * 都可以成功，但结果确实 lower = 5, upper = 4, 非法。
     *
     * NOTE: 因为lower和upper并不是独立的，所以不能将线程安全性委托给它们，
     * 即使它们自身是线程安全的。
     */

    public void setLowerBound(AtomicInteger lowerBound) {
        if (lowerBound.get() > this.upperBound.get())
            throw new IllegalArgumentException();
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(AtomicInteger upperBound) {
        if (upperBound.get() < this.lowerBound.get())
            throw new IllegalArgumentException();
        this.upperBound = upperBound;
    }

    public boolean contains(int i) {
        return lowerBound.get() <= i && upperBound.get() >= i;
    }
}

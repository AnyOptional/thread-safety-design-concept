package com.archer.composingobjects;

import com.archer.annotation.ThreadSafe;

@ThreadSafe
public class SafeRange {

    private int lower;
    private int upper;

    /**
     * 利用内置锁保证了设置lower和upper的原子性，
     * 同一时刻只能设置lower或upper而不能同时设置。
     * 这样，lower <= upper的不变约束可以得到维持。
     */

    public synchronized void setLower(int lower) {
        if (lower > this.upper)
            throw new IllegalArgumentException();
        this.lower = lower;
    }

    public synchronized void setUpper(int upper) {
        if (upper < this.lower)
            throw new IllegalArgumentException();
        this.upper = upper;
    }

    public synchronized boolean contains(int i) {
        return lower <= i && upper >= i;
    }

}

package com.archer.threadsafety;

import com.archer.annotation.ThreadSafe;

/**
 * 线程安全 因为它本身没有保存数据，
 * calculate方法仅访问了线程自有的栈空间，
 * 而线程的栈空间是不共享的。
 */
@ThreadSafe
public class StatelessCalculator {

    public int calculate(int x, int y) {
        return x + y;
    }

}

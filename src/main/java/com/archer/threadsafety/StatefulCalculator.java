package com.archer.threadsafety;

import com.archer.annotation.NotThreadSafe;

/**
 * 不是线程安全的 因为calculate方法对counter域执行了写入
 * 而getCounter方法又对counter域执行了读取，这会导致多线程
 * 并发访问的 read/write 冲突。
 */
@NotThreadSafe
public class StatefulCalculator {

    private long counter;

    public long getCounter() {
        return counter;
    }

    public int calculate(int x, int y) {
        /**
         * ++counter 并不是一个原子操作，虽然看起来挺像。
         * 它实际上是counter = counter + 1，也就是说
         * ++counter其实是三个离散操作的简写形式：获取当前值，加1，写回新值。
         *
         * NOTE：这是一个读-改-写操作的实例，其中，结果的状态衍生自它先前的状态。
         * 这种有着前后依赖关系的操作绝不是线程安全的，因为它需要在执行的过程中没有
         * 其他线程来修改counter。
         */
        ++counter;
        return x + y;
    }
}

package com.archer.threadsafety;

import com.archer.annotation.NotThreadSafe;

import java.util.concurrent.TimeUnit;

@NotThreadSafe
public class LazyInitRace {

    private ExpensiveObject object;

    /**
     * 如果线程A、B同时检测到 object == null ，
     * 就有可能创建两个不同的ExpensiveObject对象，
     * 而通常来说，懒加载只会创建一次对象。
     */
    public ExpensiveObject getInstance() {
        if (object == null) {
            /**
             * 执行到这一句需要分配ExpensiveObject大小的内存，
             * 给ExpensiveObject的属性设定初始值，
             * 执行初始化，给object引用赋值，这些都
             * 需要时间。这一句执行过程中有可能object仍是null，也有可能object
             * 是一个部分初始化了的对象，这些都是不稳定的状态。
             */
            object = new ExpensiveObject();
        }
        return object;
    }

    public static class ExpensiveObject {

        /** 模拟一个耗时的创建任务 */
        public ExpensiveObject() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ignore) {}
        }
    }

}

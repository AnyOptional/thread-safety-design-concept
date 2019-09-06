package com.archer.threadsafety;

import com.archer.annotation.NotThreadSafe;
import com.archer.annotation.ThreadSafe;

public class DCL {

    @NotThreadSafe
    static class UnsafeResource {

        private static UnsafeResource resource;

        static UnsafeResource getInstance() {
            /**
             * DCL的问题在于它基于这样一个假设：
             * 在没有使用同步的时候访问resource变量，
             * 最坏也不过看到一个过期值(也就是null)，所以
             * 它通过一个加锁的代码块来进行二次检查。然而，
             * resource可能不是null，但它持有的状态却可能
             * 是过期的，这意味着resource处于无效或错误的状态。
             */
            if (resource == null) {
                synchronized (UnsafeResource.class) {
                    if (resource == null) {
                        resource = new UnsafeResource();
                    }
                }
            }
            return resource;
        }
    }

    @ThreadSafe
    static class SafeResource {

        /**
         * 通过添加volatile关键字，保证了resource的可见性，
         * 使得它的状态在所有读线程中都保持一致。
         */
        private static volatile SafeResource resource;

        static SafeResource getInstance() {
            if (resource == null) {
                synchronized (SafeResource.class) {
                    if (resource == null) {
                        resource = new SafeResource();
                    }
                }
            }
            return resource;
        }
    }
}

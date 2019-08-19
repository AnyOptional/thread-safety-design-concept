package com.archer.buildingblocks;

import com.archer.annotation.NotThreadSafe;

import java.util.Vector;

@NotThreadSafe
public class UnsafeVectorHelper {


    public static <E> E lastObject(Vector<E> vector) {
        int index = vector.size() - 1;
        /**
         * Vector的size()和get(_:)调用都是线程安全的，
         * 不代表lastObject方法是线程安全的。在多线程并发的
         * 情况下，在size()和get(_:)的调用之间，其他线程可能
         * 删除了Vector中的元素，导致抛出out of bounds异常。
         */

        // 其他线程在执行到这里时删除了vector中的元素

        if (index < 0) return null;
        return vector.get(index);
    }

}

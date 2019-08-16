package com.archer.composingobjects;

import com.archer.annotation.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 客户端加锁会破坏同步策略的封装性。
 * 它将类C加锁代码置于与类C完全无关的类中。
 */
@ThreadSafe
public class SafeListHelper<E> {

    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    public void putIfAbsent(E e) {
        /**
         * synchronizedList由其内置锁保护，
         * 将putIfAbsent用同一把锁保护起来，就可以
         * 维持线程安全性。
         *
         * WARNING：synchronizedList线程安全的实现方式可能会在不同jdk中有所变化，
         * 这种做法依赖于synchronizedList线程安全的实现方式，显得不是那么可靠。
         */
        synchronized (list) {
            if (!list.contains(e)) {
                list.add(e);
            }
        }
    }

}

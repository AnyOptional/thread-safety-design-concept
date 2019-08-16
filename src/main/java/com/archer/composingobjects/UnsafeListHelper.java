package com.archer.composingobjects;

import com.archer.annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NotThreadSafe
public class UnsafeListHelper<E> {

    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    /**
     * putIfAbsent和list的其它操作并不是用同一把锁保护，
     * putIfAbsent相对于list的其它操作而言，并不是原子的，
     * 因而无法保证调用putIfAbsent时不会执行list的其它操作，
     * 因为不能保证线程安全性。
     */
    public synchronized void putIfAbsent(E e) {
        if (!list.contains(e)) {
            list.add(e);
        }
    }
}

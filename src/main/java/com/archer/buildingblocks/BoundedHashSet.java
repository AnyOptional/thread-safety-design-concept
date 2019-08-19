package com.archer.buildingblocks;

import com.archer.annotation.ThreadSafe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class BoundedHashSet<E> {

    /**
     * 计数信号量(counting semaphore)用来控制可同时访问某特定资源
     * 的活动的数量，或者同时执行某一给定操作的数量。
     *
     * 信号量可以用来实现资源池、有界容器等。
     */
    private final Semaphore semaphore;
    private final Set<E> set = Collections.synchronizedSet(new HashSet<>());

    public BoundedHashSet(int size) {
        semaphore = new Semaphore(size);
    }

    public boolean add(E e) throws InterruptedException {
        // 能不能添加要看能否从信号量处获得许可
        semaphore.acquire();
        boolean added = set.add(e);
        // 对HashSet来说，重复的元素是添加不了的
        // 如果没有添加进去，自然要归还许可
        if (!added) {
            semaphore.release();
        }
        return added;
    }

    public boolean remove(E e) throws InterruptedException {
        // 同样，对于删除而言，真正的删除才需要归还许可
        boolean removed = set.remove(e);
        if (removed) {
            semaphore.release();
        }
        return removed;
    }

}

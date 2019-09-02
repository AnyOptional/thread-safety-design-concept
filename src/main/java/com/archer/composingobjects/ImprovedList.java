package com.archer.composingobjects;

import com.archer.annotation.ThreadSafe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 使用组合向已有的类中添加一个原子操作。
 * ImprovedList通过将底层的操作代理给list，从而实现了List接口，
 * 同时添加了一个原子的putIfAbsent方法。
 * ImprovedList使用自身的内置锁来保持线程安全性，从而使得其不再依赖于
 * list本身的同步策略，更进一步的，不管list本身线程安全与否，ImprovedList
 * 的所有操作都是线程安全的。
 *
 * NOTE：装饰者模式
 */
@ThreadSafe
public class ImprovedList<E> implements List<E> {

    private final List<E> list;

    public ImprovedList(List<E> list) {
        this.list = list;
    }

    public synchronized void putIfAbsent(E e) {
        if (!list.contains(e)) {
            list.add(e);
        }
    }

    @Override
    public synchronized int size() {
        return list.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public synchronized Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return list.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public synchronized boolean add(E e) {
        return list.add(e);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        list.clear();
    }

    @Override
    public synchronized E get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public synchronized void add(int index, E element) {
        list.add(index, element);
    }

    @Override
    public synchronized E remove(int index) {
        return list.remove(index);
    }

    @Override
    public synchronized int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public synchronized ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public synchronized ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public synchronized List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}

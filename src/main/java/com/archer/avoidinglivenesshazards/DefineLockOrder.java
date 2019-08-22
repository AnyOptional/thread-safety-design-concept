package com.archer.avoidinglivenesshazards;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class DefineLockOrder {

    private static final Object onSameLock = new Object();

    public static void doSomething(Object param0, Object param1) {
        /**
         * 通过对象的hash码来决定加锁的顺序，保证加锁的次序一致
         *
         * 如果Object具有唯一的、不可变的并且具有可比性的标识，用这个标识
         * 来决定锁顺序就更好了。
         */
        int hash0 = Objects.hashCode(param0);
        int hash1 = Objects.hashCode(param1);
        if (hash0 > hash1) {
            synchronized (param0) {
                Thread.yield();
                synchronized (param1) {
                    System.out.println("do something");
                }
            }
        } else if (hash0 < hash1) {
            synchronized (param1) {
                Thread.yield();
                synchronized (param0) {
                    System.out.println("do something");
                }
            }
        } else {
            /**
             * 如果出现两个对象hashCode相同的情况，引入另一个独立的锁，
             * 所有线程在访问时需要先获得这个独立的锁(同一时间只有一个线程能获得)，
             * 通过这种方式避免出现锁顺序死锁。
             */
            synchronized (onSameLock) {
                synchronized (param0) {
                    Thread.yield();
                    synchronized (param1) {
                        System.out.println("do something");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Object other = new Object();
        Object one = new Object();
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    doSomething(one, other);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    doSomething(one, one);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    doSomething(other, other);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    doSomething(other, one);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        latch.countDown(); // never get deadlock
    }
}

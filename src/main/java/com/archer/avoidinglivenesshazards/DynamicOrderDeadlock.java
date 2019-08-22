package com.archer.avoidinglivenesshazards;

import java.util.concurrent.CountDownLatch;

public class DynamicOrderDeadlock {

    /**
     * 所有线程看起来都是以相同的顺序获得锁的，先param0后param1.
     * 然而实际上锁的顺序取决于传递给doSomething参数的顺序，而参数
     * 的顺序取决于外部输入。
     *
     * 如果两个线程同时调用doSomething，一个传递参数从A到B，另一个
     * 从B到A，那么就会发生死锁
     */
    public static void doSomething(Object param0, Object param1) {
        synchronized (param0) {
            Thread.yield();
            synchronized (param1) {
                System.out.println("do something");
            }
        }
    }

    public static void main(String[] args) {

        Object one = new Object();
        Object other = new Object();
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
                    doSomething(other, one);
                } catch (InterruptedException e) {

                }
            }
        }).start();

        latch.countDown(); // deadlock occurs
    }
}

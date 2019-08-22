package com.archer.avoidinglivenesshazards;

import java.util.concurrent.CountDownLatch;

/**
 * 锁顺序死锁
 */
public class LeftRightDeadlock {

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        synchronized (left) {
            Thread.yield();
            synchronized (right) {
                System.out.println("left to right");
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            Thread.yield();
            synchronized (left) {
                System.out.println("right to left");
            }
        }
    }

    public static void main(String[] args) {

        /**
         * LeftRightDeadlock发生死锁的原因：两个线程以不同的顺序获得多个相同的锁。
         *
         * 若线程A调用leftRight()获得锁left还未获得锁right时，线程B调用rightLeft()
         * 获得锁right，此时线程A，B都无法继续进行下去。
         */

        LeftRightDeadlock instance = new LeftRightDeadlock();
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    instance.leftRight();
                } catch (InterruptedException e) {

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    instance.rightLeft();
                } catch (InterruptedException e) {

                }
            }
        }).start();

        latch.countDown(); // deadlock occurs
    }
}

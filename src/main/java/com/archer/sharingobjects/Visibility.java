package com.archer.sharingobjects;

public class Visibility {

    private volatile static int number;
    private volatile static boolean ready;

    private static class ReadThread extends Thread {

        /**
         * volatile可以提供内存可见性,
         * 读线程可以观察到主线程设置了ready = true，不会产生死循环。
         * 然而，ready和number的设置并不是原子的，打印的结果可能是15
         * 也可能是0。
         */
        @Override
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReadThread().start();
        ready = true;
        /**
         * Thread.yield()增大了打印0的几率
         */
        Thread.yield();
        number = 15;
    }
}


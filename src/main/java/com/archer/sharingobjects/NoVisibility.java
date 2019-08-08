package com.archer.sharingobjects;

public class NoVisibility {

    private static int number;
    private static boolean ready;

    private static class ReadThread extends Thread {

        /**
         * ReadThread观察到ready为true时，会打印number的值。
         *
         * 这里可能会打印15，可能会打印0，也可能产生死循环。
         * - 打印15：主线程所有操作发生在读线程之前，并且读线程能够读取到主线程所做的修改
         * - 打印0：发生了指令重排序。代码上number在ready之前设置，JVM可能重排序导致ready在number之前设置。
         * - 死循环：读线程不能观察到主线程所做的修改
         */
        @Override
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        /**
         * 主线程启动ReadThread，并修改状态
         */
        new ReadThread().start();
        number = 15;
        ready = true;
    }
}

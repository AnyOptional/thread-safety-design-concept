package com.archer.sharingobjects;

public class AtomicVisibility {

    private static final Object lock = new Object();

    private static int number;
    private static boolean ready;

    private static class ReadThread extends Thread {

        /**
         * 使用同步保证了设置ready和number的原子性。
         * 读线程可能在主线程设置变量之前运行，若如此，会进入while产生死循环。
         * 因为读线程没有释放锁，因而主线程不可能获得这个锁，也就设置不了变量，
         * wait的作用也就在于此。
         */
        @Override
        public void run() {
            synchronized (lock) {
                while (!ready) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {}
                }
                System.out.println(number);
            }
        }
    }

    public static void main(String[] args) {
        new ReadThread().start();
        synchronized (lock) {
            ready = true;
            Thread.yield();
            number = 15;
            lock.notifyAll();
        }
    }
}

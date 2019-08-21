package com.archer.buildingblocks;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁(latch)可以用来确保特定活动直到其他活动完成后才发生。
 * 闭锁是一次性的(one-shot)，一旦进入最终状态，就不能被重置。
 */
public class TestHarness {

    public long timeTasks(final int nTasks, Runnable task) throws InterruptedException {

        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nTasks);
        for (int i = 0; i < nTasks; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /**
                         * 线程的启动有快有慢，在这里等待
                         * 确保所有线程可以一同开始执行
                         */
                        startGate.await();
                        /**
                         * 每执行完一个任务，令计数器减一
                         */
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) { }
                }
            }).start();
        }
        /**
         * nTasks个线程创建好后，使这些线程一同执行
         */
        startGate.countDown();
        /**
         * 在所有task都执行前后计时，记录并发执行的时间
         */
        long start = System.nanoTime();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}

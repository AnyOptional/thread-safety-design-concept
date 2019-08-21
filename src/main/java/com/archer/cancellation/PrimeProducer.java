package com.archer.cancellation;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) {
                p = p.nextProbablePrime();
                queue.put(p);
            }
        } catch (InterruptedException ignore) {
            System.out.println("producer interrupted");
        }
    }

    void cancel() {
        /**
         * 中断通常是实现取消最明智的选择。
         *
         * 对于中断，它并不会真正的中断一个正在运行的线程，
         * 它仅仅发出中断请求，线程会在下一个方便的时刻中断。
         *
         * NOTE：静态的interrupted()方法要谨慎使用，因为它会清除线程的
         * 中断状态，如果调用interrupted()返回true，要么就处理中断，要么
         * 就重新抛出InterruptedException，或者重新调用interrupt()重置
         * 中断状态。
         */
        interrupt();
    }

    public static void main(String[] args)  {
        BlockingQueue<BigInteger> queue = new ArrayBlockingQueue<>(10);
        PrimeProducer producer = new PrimeProducer(queue);
        producer.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        BigInteger integer = queue.take();
                        System.out.println("ret = " + integer);
                        TimeUnit.SECONDS.sleep(1);
                    }
                } catch (InterruptedException e) {
                    System.out.println("consumer interrupted");
                }
            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {

        } finally {
            producer.cancel();
        }
    }
}
package com.archer.threadpool;

import java.util.concurrent.*;

public class Deadlock {

    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newSingleThreadExecutor();

        /**
         * 在单线程的Executor中，在一个任务中提交另一个任务，
         * 并且等待被提交任务的结果，会引发死锁。
         *
         * 因为Executor是FIFO的，又是单线程，因此先提交的任务会先完成。
         * 任务A提交任务B到Executor并等待任务B的结果，任务B滞留在工作队列中，
         * 等待任务A完成，而任务A不会完成，因为它在等待任务B完成，这样形成了相互等待，产生死锁。
         *
         * 在一个大的线程池中，如果所有线程执行的任务都阻塞在线程池中，等待着仍然处于同一工作队列
         * 中的其他任务，会发生线程饥饿死锁(thread starvation deadlock)。
         *
         * 线程池任务开始了无限期等待，其目的是等待一些资源或条件, 此时只有另一个线程池任务的活动
         * 才能使条件成立，比如等待返回值或另一个线程任务的side effect, 除非能保证这个池足够大，否则
         * 会产生线程饥饿死锁。
         */
        service.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("task A command executed");
                Future<Object> future = service.submit(new Callable<Object>() {
                    @Override
                    public String call() throws Exception {
                        System.out.println("task B command executed"); // would never execute
                        return null;
                    }
                });
                try {
                    future.get(); // dead lock occurs
                } catch (Exception e) {}
            }
        });

        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
    }
}

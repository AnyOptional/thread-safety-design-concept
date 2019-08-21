package com.archer.taskexecution;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class ReplacementForTimer {


    static class OutOfTime {
        public static void main(String[] args) throws InterruptedException {
            /**
             * Timer存在诸多缺陷。
             * - 只创建唯一的一个线程来执行所有调度任务，若某个任务耗时很长，超过了其他任务等待调度的时间，
             * 其他任务可能在这个任务执行完后迅速执行完毕也可能被丢弃(看实现了)。
             * - Timer线程并不捕获异常，因此如果TimerTask抛出未检查的异常，Timer线程就会被终止，其他任务也
             * 就得不到调度了。
             */
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    throw new RuntimeException();
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, 1);
            TimeUnit.SECONDS.sleep(1);
            timer.schedule(task, 1);
            TimeUnit.SECONDS.sleep(3);
            System.out.println("main ended");
        }
    }

    public static void main(String[] args) {
        /**
         * 使用ScheduledExecutorService作为Timer的替代品
         */
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(3);
        service.scheduleAtFixedRate(() -> System.out.println("timer task"), 0, 2, TimeUnit.SECONDS);
    }
}

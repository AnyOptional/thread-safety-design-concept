package com.archer.buildingblocks;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Preloader {

    private final FutureTask<String> task = new FutureTask<>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return loadJSON();
        }
    });

    private String loadJSON() {
        System.out.println("load method invoked");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "hello world";
    }

    private Thread thread = new Thread(task);

    public void start() {
        thread.start();
    }

    public String get() throws InterruptedException {
        try {
            return task.get();
        } catch (ExecutionException e) {
            throw  new RuntimeException();
        }
    }

    public static void main(String[] args) throws Exception {
        Preloader preloader = new Preloader();
        // start之后会开始执行call方法
        preloader.start();
        // 调用get用以获取结果(从call返回)
//        preloader.get();
    }
}

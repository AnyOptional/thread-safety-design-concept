package com.archer.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
            ServerSocket socket = new ServerSocket(80);
            while (true) {
                Socket connection = socket.accept();
                /**
                 * Executor将任务的提交和执行进行了解耦。
                 * 它的价值在于让我们可以对一个类的给定任务定制执行策略。
                 *
                 * - 体会一下Executor抽象带来的扩展性
                 */
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(connection);
                    }
                });
            }
        } catch (IOException e) {}
    }

    private static void handleRequest(Socket socket) {
        // do something
    }

}

/**
 * 实现类似ThreadPerTaskWebServer的行为
 */
class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}

/**
 * 实现类似SingleThreadWebServer的行为
 */
class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}


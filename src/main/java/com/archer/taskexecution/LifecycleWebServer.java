package com.archer.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class LifecycleWebServer {

    /**
     * ExecutorService在Executor的基础上扩展了管理生命周期的方法。
     */
    private final ExecutorService service = Executors.newCachedThreadPool();

    public void start() {
        try {
            ServerSocket socket = new ServerSocket(80);
            while (!service.isShutdown()) {
                try {
                    Socket connection = socket.accept();
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            handleRequest(connection);
                        }
                    });
                } catch (RejectedExecutionException e) {
                    if (!service.isShutdown()) {
                        System.out.println("can not accept new command");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        service.shutdown();
    }

    private void handleRequest(Socket socket) {
        // do something
    }

    public static void main(String[] args) {
        LifecycleWebServer webServer = new LifecycleWebServer();
        webServer.start();
    }
}

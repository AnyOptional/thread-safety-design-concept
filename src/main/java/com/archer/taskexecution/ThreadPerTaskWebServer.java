package com.archer.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadPerTaskWebServer {

    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(80);
            /**
             * 为每一个请求都创建一个单独的线程来处理。
             * 这种方法相比于单线程处理所有请求提高了吞吐量和资源利用率，
             * 然而系统的资源始终是有限的，线程的数量也不是越多越好，在高并发
             * 的时候，这种方式可能会导致比较严重的后果。
             */
            while (true) {
                final Socket connection = socket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(connection);
                    }
                }).start();
            }

        } catch (IOException e) {

        }
    }

    private static void handleRequest(Socket socket) {
        // do something
    }
}

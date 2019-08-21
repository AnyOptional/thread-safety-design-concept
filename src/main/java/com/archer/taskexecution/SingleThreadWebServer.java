package com.archer.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadWebServer {

    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(80);
            /**
             * 只在一个线程里处理请求。
             * accept可能阻塞很久，
             * handleRequest也可能有磁盘IO，
             * 这种方式吞吐量和资源利用率都极低。
             */
            while (true) {
                Socket connection = socket.accept();
                handleRequest(connection);
            }

        } catch (IOException e) {

        }
    }

    private static void handleRequest(Socket socket) {
        // do something
    }
}

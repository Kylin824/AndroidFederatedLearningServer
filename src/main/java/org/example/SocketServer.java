package org.example;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) {
        try {
            // 创建服务端socket
            ServerSocket serverSocket = new ServerSocket(8088);

            // 创建客户端socket
            Socket socket;

            //循环监听等待客户端的连接

            System.out.println("开始监听，等待客户端连接...");
            while (true) {
                // 监听客户端
                socket = serverSocket.accept(); // 与每个客户端建立一个socket

                ServerThread thread = new ServerThread(socket);

                thread.start();

                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP：" + address.getHostAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

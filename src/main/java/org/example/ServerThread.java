package org.example;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        OutputStream outputStream = null;
        PrintWriter printWriter = null;
        while (true) {
            if (socket != null) {
                try {
                    // 接收
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    br = new BufferedReader(inputStreamReader);
                    String info;
                    if ((info = br.readLine()) != null) {
                        System.out.println("我是服务器，客户端说：" + info);

                        // 发送
                        printWriter = new PrintWriter(outputStream);
                        printWriter.write("服务器欢迎你");
                        printWriter.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

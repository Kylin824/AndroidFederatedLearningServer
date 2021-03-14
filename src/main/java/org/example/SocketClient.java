package org.example;

import java.io.*;
import java.net.Socket;

/**
 * Hello world!
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            // 和服务器创建连接
            Socket socket = new Socket("localhost", 8088);

            // 要发送给服务器的信息
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.write("客户端发送信息");
            printWriter.flush();

            socket.shutdownOutput();

            // 从服务器接收的信息
            InputStream inputStream = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String info;
            while ((info = br.readLine()) != null) {
                System.out.println("我是客户端，服务器返回信息：" + info);
            }

            br.close();
            inputStream.close();
            outputStream.close();
            printWriter.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

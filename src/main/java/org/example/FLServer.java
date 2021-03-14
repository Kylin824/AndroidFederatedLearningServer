package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class FLServer {
    public static void main(String[] args) throws IOException {
        // 创建 http 服务器, 绑定本地 8080 端口
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        // 创建上下文监听, "/" 表示匹配所有 URI 请求
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                /*
                 * PS: 必须按顺序设置响应: 添加响应头, 发送响应码和内容长度, 写出响应内容, 关闭处理器
                 */
                // 响应内容
                byte[] respContents = "Hello World".getBytes(StandardCharsets.UTF_8);

                // 设置响应头
                httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
                // 设置响应code和内容长度
                httpExchange.sendResponseHeaders(200, respContents.length);

                // 设置响应内容
                httpExchange.getResponseBody().write(respContents);

                // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
                httpExchange.close();
            }
        });

        // 启动服务
        httpServer.start();
    }
}

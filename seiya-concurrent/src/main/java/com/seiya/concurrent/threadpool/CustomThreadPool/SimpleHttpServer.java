package com.seiya.concurrent.threadpool.CustomThreadPool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 简单的多线程处理请求web服务器
 */
public class SimpleHttpServer {

    private static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<HttpRequestHandler>(1);
    // SimpleHttpServer访问的根路径
    private static String basePath = "D:/webapps";
    // socket服务端
    private static ServerSocket serverSocket;
    // 服务监听端口
    private static int port = 8080;

    // 启动socket服务
    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(port);
        Socket socket = null;
        while ((socket = serverSocket.accept())!= null) {
            // 接收一个客户端Socket，生成一个HttpRequestHandler，放入线程池执行
            threadPool.submit(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    // 异步处理请求请求
    static class HttpRequestHandler implements Runnable {

        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            BufferedReader br = null;
            PrintWriter out = null;
            InputStream in = null;
            String line = null;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                String header = reader.readLine();
                // 由相对路径计算出绝对路径
                String filePath = basePath + header.split(" ")[1];

                File file = new File(filePath);
                if (!file.exists()) {
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println("");
                    out.println("<html><body><h2>404 not found!</h2></body></html>");
                } else if (filePath.endsWith("jpg") || filePath.endsWith("ico")) {
                    // 如果请求资源的后缀为jpg或者ico，则读取资源并输出
                    in = new FileInputStream(file);
                    ByteArrayOutputStream baio = new ByteArrayOutputStream();
                    int b = 0;
                    while ((b = in.read()) != -1) {
                        baio.write(b);
                    }
                    byte[] bytes = baio.toByteArray();
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: image/jpeg");
                    out.println("Content-Length: " + bytes.length);
                    out.println("");
                    socket.getOutputStream().write(bytes, 0, bytes.length);
                } else {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Molly");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println("");
                    while ((line = br.readLine()) != null) {
                        out.println(line);
                    }
                }
                out.flush();
            } catch (Exception e) {
                out.println("HTTP/1.1 500");
                out.println("");
                out.flush();
            } finally {
                close(reader, in, br, out, socket);
            }
        }

        // 关闭流或者Socket
        private static void close(Closeable... closeables) {
            if (closeables != null) {
                for (Closeable closeable : closeables) {
                    try {
                        closeable.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

}

package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        server.createContext("/", (exchange -> {
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            String htmlResponse = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Docker Test</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background: #1a1a1a;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            height: 100vh;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #00ff9d;\n" +
                "            font-size: 4em;\n" +
                "            text-shadow: 2px 2px 5px rgba(0,255,157,0.5);\n" +
                "            animation: glow 1s ease-in-out infinite alternate;\n" +
                "        }\n" +
                "        @keyframes glow {\n" +
                "            from { text-shadow: 0 0 10px #00ff9d; }\n" +
                "            to { text-shadow: 0 0 20px #00ff9d, 0 0 30px #00ff9d; }\n" +
                "        }\n" +
                "        .timestamp {\n" +
                "            color: #cccccc;\n" +
                "            font-size: 1.2em;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Hello James! You are the best in the world</h1>\n" +
                "        <div class=\"timestamp\">Server Time: " + timestamp + "</div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(htmlResponse.getBytes());
            }
        }));

        server.start();
        System.out.println("Server started on port " + port);
    }
}

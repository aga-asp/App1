package org.example.server;
import com.sun.net.httpserver.HttpHandler;
import org.example.reimbursement.ReimbursementHandler;
import org.example.login.LoginHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
public class HttpServer {
    private static final int port = 8080;
    public static void startServer() throws IOException {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        creteMyContext(server, "/login", new LoginHandler());
        creteMyContext(server, "/reimbursement", new ReimbursementHandler());
        server.start();
        System.out.println("Server started on port " + port);

    }
     static  <T extends HttpHandler> void creteMyContext(com.sun.net.httpserver.HttpServer server, String direction, T handler){
        try {
            server.createContext(direction, handler);
        }catch (Exception e){
            System.out.println("Error starting server");
        }
    }
}

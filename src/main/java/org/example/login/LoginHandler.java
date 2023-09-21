package org.example.login;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.common_classes.QueryParametersMapper;
import org.example.custom_errors.BadUserNameOrPasswordException;
import org.example.response.Response;

import java.io.IOException;
public class LoginHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET" -> handleLoginGetRequest(httpExchange);
            case "POST" -> System.out.println("post");
            default -> {
            }
        }
    }
    private void handleLoginGetRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (LoginService.returnLoggedUser(QueryParametersMapper.mapQueryParameters(query))!=null) {
                Response.sendResponse(httpExchange, 200, "Login successful!");
            }
        } catch (BadUserNameOrPasswordException | IOException e) {
            Response.sendResponse(httpExchange, 401, "Bad username or password");
        }
    }

    //        String query = httpExchange.getRequestURI().getQuery();
//        try {
//            if (LoginService.getLoggedUser(QueryParametersMapper.mapQueryParameters(query, 2, "&", "="))!=null) {
//                Response.sendResponse(httpExchange, 200, "Login successful!");
//            }
//        } catch (BadUserNameOrPasswordException e) {
//            Response.sendResponse(httpExchange, 401, "Bad username or password");
//        }


//    static class MyHandler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("GET".equals(exchange.getRequestMethod())) {
//                handleGetRequest(exchange);
//            } else if ("POST".equals(exchange.getRequestMethod())) {
//                handlePostRequest(exchange);
//            }
//        }
}
package org.example.login;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.mappersandreaders.QueryParametersMapper;
import org.example.customexceptions.InvalidCredentials;
import org.example.response.ResponseSender;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginHandler implements HttpHandler {
    LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String methodString = httpExchange.getRequestMethod();
        String queryString = httpExchange.getRequestURI().toString();
        Pattern pattern = Pattern.compile("/login\\?username=([^&]+)&password=([^&]+)");
        Matcher matcher = pattern.matcher(queryString);
//
//        Map<Pattern, Function<HttpExchange, Object>> requestHandlerMap = new HashMap<>();
//        {
//            requestHandlerMap.put(pattern,this::returnLoggedUser)
//        }
        switch (methodString) {
            case "GET" -> {
                if (matcher.find()) {
                    handleLoginGetRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            case "POST" -> {
                if (matcher.find()) {
                    handleLoginPostRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            default -> ResponseSender.sendResponse(httpExchange, 404, "Operation not supported by the server");
        }
    }

    //--TODO Post method--
    private void handleLoginPostRequest(HttpExchange httpExchange) {
        System.out.println("empty post");
    }

    private void handleLoginGetRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (loginService.returnLoggedUser(QueryParametersMapper.mapToQueryParameters(query)) != null) {
                ResponseSender.sendResponse(httpExchange, 200, "Login successful!");
            }
        } catch (InvalidCredentials e) {
            ResponseSender.sendResponse(httpExchange, 401, "Bad username or password");
        }
    }
}
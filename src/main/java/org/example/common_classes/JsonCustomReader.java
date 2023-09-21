package org.example.common_classes;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonCustomReader {
    public static String readJson(HttpExchange httpExchange) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return String.valueOf(response);
        
    }

}

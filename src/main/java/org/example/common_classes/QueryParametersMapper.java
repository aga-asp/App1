package org.example.common_classes;

import java.util.HashMap;
import java.util.Map;

public class QueryParametersMapper {
    public static Map<String, String> mapQueryParameters(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] keyValuePairs = query.split("&");
            for (String keyValue : keyValuePairs) {
                String[] pair = keyValue.split("=");
                String key = pair[0];
                String value = pair[1];
                if (pair.length == 2) {
                    params.put(key, value);
                }
            }
        }
        return params;
    }
}

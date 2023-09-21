package org.example.common_classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//Bo po prostu lubię utrudniać sobie życie
public class QueryParametersMapperMultipleValues {
    public static Map<String, String[]> mapQueryParameters(String query) {
        Map<String, String[]> params = new HashMap<>();
        if (query != null) {
            String[] keyValuePairs = query.split("&");
            for (String keyValue : keyValuePairs) {
                String[] pair = keyValue.split("=");
                String key = pair[0];
                String[] values = pair[1].split(",");
                if (pair.length == 2) {
                    params.put(key, values);
                }
            }
        }
        if (params.size() > 1) {
            throw new UnknownError();
        }
        //params.forEach((key, value) -> System.out.println(key + "/" + Arrays.toString(value)));
        return params;
    }
}

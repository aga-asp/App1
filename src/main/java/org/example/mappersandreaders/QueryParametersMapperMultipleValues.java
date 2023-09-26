package org.example.mappersandreaders;

import org.example.customexceptions.IncorrectQuery;

import java.util.HashMap;
import java.util.Map;

public class QueryParametersMapperMultipleValues {
    public static Map<String, String[]> mapToQueryParameters(String query) throws IncorrectQuery {
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
            throw new IncorrectQuery();
        }
        return params;
    }
}

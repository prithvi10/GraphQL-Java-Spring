package com.example.graphqldemo.fetch;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class WandFetcher {
    public DataFetcher getData() {
        return environment -> {
            Map<String, Object> wand = new HashMap<>();
            wand.put("name", "Elder Wand");
            wand.put("origin", "elder wood");
            return wand;
        };

    }
}

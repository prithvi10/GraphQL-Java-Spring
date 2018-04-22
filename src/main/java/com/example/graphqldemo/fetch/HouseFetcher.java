package com.example.graphqldemo.fetch;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HouseFetcher {
    public DataFetcher getData() {
        return environment -> {
            Map<String, Object> house = new HashMap<>();
            house.put("name", "Griffindor");
            house.put("color", "red");
            house.put("points", 190);
            return house;
        };
    }
}

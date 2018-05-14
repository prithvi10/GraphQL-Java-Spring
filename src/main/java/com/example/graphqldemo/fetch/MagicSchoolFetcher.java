package com.example.graphqldemo.fetch;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MagicSchoolFetcher {

    public DataFetcher getData() {
        return environment -> {

            Map<String, Object> school = new HashMap<>();
            school.put("HeadMaster", "Albus Dumbledore");
            school.put("name", "Hogwards");
            return school;
        };
    }
}


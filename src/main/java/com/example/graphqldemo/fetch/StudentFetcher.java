package com.example.graphqldemo.fetch;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StudentFetcher {

    public DataFetcher getData() {
        return environment -> {
            Map<String, Object> student = new HashMap<>();
            if ("1".equalsIgnoreCase(environment.getArgument("id"))) {
                student.put("name", "Harry Potter");
            }
            return student;
        };
    }

    /*Used by mutation*/
    public DataFetcher putData() {
        return environment -> {
            /*Put the data into datastore */
            return "Student Successfully Enrolled";
        };
    }
}

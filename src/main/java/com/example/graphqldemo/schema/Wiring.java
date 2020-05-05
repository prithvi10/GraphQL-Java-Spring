package com.example.graphqldemo.schema;


import com.example.graphqldemo.AuthDirective;
import com.example.graphqldemo.fetch.HouseFetcher;
import com.example.graphqldemo.fetch.MagicSchoolFetcher;
import com.example.graphqldemo.fetch.StudentFetcher;
import com.example.graphqldemo.fetch.WandFetcher;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Wiring {
    @Autowired
    StudentFetcher studentFetcher;
    @Autowired
    WandFetcher wandFetcher;
    @Autowired
    HouseFetcher houseFetcher;
    @Autowired
    MagicSchoolFetcher magicSchoolFetcher;

    public RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring
                        .dataFetcher("magicSchool", magicSchoolFetcher.getData())
                )
                .type("MagicSchool", typeWiring -> typeWiring
                        .dataFetcher("student", studentFetcher.getData())
                )
                .type("Student", typeWiring -> typeWiring
                        .dataFetcher("wand", wandFetcher.getData())
                        .dataFetcher("house", houseFetcher.getData())
                )
                .type("Mutation", typeWiring -> typeWiring
                        .dataFetcher("enrollStudent", studentFetcher.putData())
                )
                .directive("auth", new AuthDirective())
                .build();
    }
}


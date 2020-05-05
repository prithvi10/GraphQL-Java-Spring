package com.example.graphqldemo;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.Map;

public class AuthDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> directiveWiringEnvironment) {
        String client = (String) directiveWiringEnvironment.getDirective().getArgument("client").getValue();
        GraphQLFieldDefinition field = directiveWiringEnvironment.getElement();

        DataFetcher originalDF = field.getDataFetcher();
        DataFetcher authDF = environment -> {
            Map<String, Object> contextMap = environment.getContext();
            if (client == contextMap.get("clientId"))
                return originalDF.get(environment);

            else return null;
        };
        return field.transform(builder -> builder.dataFetcher(authDF));
    }
}

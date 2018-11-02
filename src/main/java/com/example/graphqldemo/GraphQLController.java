package com.example.graphqldemo;

import com.example.graphqldemo.schema.Wiring;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;


@RestController
public class GraphQLController {
    @Autowired
    Wiring wiring;

    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public Map<String,Object> myGraphql(@RequestBody String request) throws Exception {

        JSONObject jsonRequest = new JSONObject (request);

        SchemaParser schemaParser = new SchemaParser();
        File schemaFile = new File("src/main/java/com/example/graphqldemo/magicSchool.graphql");

        TypeDefinitionRegistry typeDefinitionRegistry =schemaParser.parse(schemaFile);


        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring.buildRuntimeWiring());
        GraphQL build = GraphQL.newGraphQL(graphQLSchema).instrumentation(new MyInstrumentation()).build();

        ExecutionInput executionInput=ExecutionInput.newExecutionInput().query(jsonRequest.getString("query")).build();
        ExecutionResult executionResult=build.execute(executionInput);

        return executionResult.toSpecification();
    }
}

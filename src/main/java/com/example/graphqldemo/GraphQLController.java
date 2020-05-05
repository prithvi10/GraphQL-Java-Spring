package com.example.graphqldemo;

import com.example.graphqldemo.schema.Wiring;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Map;

@RestController
public class GraphQLController {
    @Autowired
    Wiring wiring;

    private GraphQL graphQl;

    @PostConstruct
    protected void init() {
        graphQl = getGraphQl();
    }

    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public Map<String, Object> myGraphql(@RequestBody String request) throws Exception {

        JSONObject jsonRequest = new JSONObject(request);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(jsonRequest.getString("query")).build();
        ExecutionResult executionResult = graphQl.execute(executionInput);

        return executionResult.toSpecification();
    }

    public GraphQL getGraphQl() {
        SchemaParser schemaParser = new SchemaParser();
        File schemaFile = new File("src/main/java/com/example/graphqldemo/magicSchool.graphql");
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring.buildRuntimeWiring());
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @RequestMapping(value = "/graphql/reload", method = RequestMethod.POST, produces = "application/json")
    public String myGraphqlReload(@RequestBody String request) throws Exception {

        SchemaParser schemaParser = new SchemaParser();
        File schemaFile = new File("src/main/java/com/example/graphqldemo/magicSchool1.graphql");
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaFile);

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring.buildRuntimeWiring());
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring.buildRuntimeWiring());
        this.graphQl = GraphQL.newGraphQL(graphQLSchema).build();
        return "Successfully reloaded!!";
    }
}


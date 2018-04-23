import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@RestController
public class SimpleController {

    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public Map<String, Object> myGraphql(@RequestBody String request) throws Exception {
        String schema = "type Query{MagicSchool: String}";
        JSONObject jsonRequest = new JSONObject(request);

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("MagicSchool", new StaticDataFetcher("Hogwards")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(jsonRequest.getString("query")).build();
        ExecutionResult executionResult = build.execute(executionInput);

        return executionResult.toSpecification();
    }
}

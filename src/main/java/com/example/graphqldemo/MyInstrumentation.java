package com.example.graphqldemo;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.ExecutionContext;
import graphql.execution.instrumentation.*;
import graphql.execution.instrumentation.parameters.*;
import graphql.execution.instrumentation.tracing.TracingSupport;
import graphql.language.Document;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static graphql.execution.instrumentation.SimpleInstrumentationContext.whenCompleted;

/**
 *
 */
public class MyInstrumentation implements Instrumentation {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyInstrumentation.class);

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        TracingSupport tracingSupport = parameters.getInstrumentationState();
        LinkedHashMap<Object, Object> tracing = new LinkedHashMap<>();
        LOGGER.debug("Appending duration");
        tracing.put("Total-ResponseTime", (Long) tracingSupport.snapshotTracingData().get("duration") / 1000000);
        ArrayList fieldData = (ArrayList) ((Map<String, Object>) tracingSupport.snapshotTracingData().get("execution")).get("resolvers");
        for (int index = 0; index < fieldData.size(); index++) {
            tracing.put(((LinkedHashMap<String, Object>) fieldData.get(index)).get("fieldName").toString() + "-Time", (Long) ((LinkedHashMap<String, Object>) fieldData.get(index)).get("duration") / 1000000);
        }
        return CompletableFuture.completedFuture(new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(), tracing));
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        TracingSupport tracingSupport = parameters.getInstrumentationState();
        TracingSupport.TracingContext ctx = tracingSupport.beginField(parameters.getEnvironment(),true);
        return whenCompleted((result, t) -> ctx.onEnd());
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginFieldComplete(InstrumentationFieldCompleteParameters parameters) {
        return null;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginFieldListComplete(InstrumentationFieldCompleteParameters parameters) {
        return null;
    }

    @Override
    public ExecutionInput instrumentExecutionInput(ExecutionInput executionInput, InstrumentationExecutionParameters parameters) {
        return null;
    }

    @Override
    public GraphQLSchema instrumentSchema(GraphQLSchema schema, InstrumentationExecutionParameters parameters) {
        return null;
    }

    @Override
    public ExecutionContext instrumentExecutionContext(ExecutionContext executionContext, InstrumentationExecutionParameters parameters) {
        return null;
    }

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        return null;
    }


    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public InstrumentationContext<Document> beginParse(InstrumentationExecutionParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public InstrumentationContext<List<ValidationError>> beginValidation(InstrumentationValidationParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public ExecutionStrategyInstrumentationContext beginExecutionStrategy(InstrumentationExecutionStrategyParameters parameters) {
        return null;
    }

    @Override
    public DeferredFieldInstrumentationContext beginDeferredField(InstrumentationDeferredFieldParameters parameters) {
        return null;
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

}

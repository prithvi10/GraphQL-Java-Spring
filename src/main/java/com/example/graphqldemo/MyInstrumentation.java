package com.example.graphqldemo;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.InstrumentationState;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.*;
import graphql.execution.instrumentation.tracing.TracingSupport;
import graphql.language.Document;
import graphql.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static graphql.execution.instrumentation.SimpleInstrumentationContext.whenCompleted;

/**
 * Created by quang.nguyen on 2017/12/13.
 */
public class MyInstrumentation implements Instrumentation {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyInstrumentation.class);
    @Override
    public InstrumentationState createState() {
        return new TracingSupport();
    }

    @Override
    public CompletableFuture<ExecutionResult> instrumentExecutionResult(ExecutionResult executionResult, InstrumentationExecutionParameters parameters) {
        TracingSupport tracingSupport = parameters.getInstrumentationState();
        LinkedHashMap<Object,Object> tracing=new LinkedHashMap<> ();
        LOGGER.debug("Appending duration");
        tracing.put ("Total-ResponseTime",(Long) tracingSupport.snapshotTracingData ().get ("duration")/1000000);
        ArrayList fieldData=(ArrayList) ((Map<String, Object>) tracingSupport.snapshotTracingData ().get ("execution")).get("resolvers");
        for(int index=0;index<fieldData.size();index++) {
            tracing.put (((LinkedHashMap<String,Object>)fieldData.get (index)).get ("fieldName").toString ()+"-Time",(Long) ((LinkedHashMap<String,Object>)fieldData.get (index)).get("duration")/1000000);
        }
        return CompletableFuture.completedFuture(new ExecutionResultImpl(executionResult.getData(), executionResult.getErrors(),tracing));
    }

    @Override
    public InstrumentationContext<Object> beginFieldFetch(InstrumentationFieldFetchParameters parameters) {
        TracingSupport tracingSupport = parameters.getInstrumentationState();
        TracingSupport.TracingContext ctx = tracingSupport.beginField(parameters.getEnvironment());
        return whenCompleted((result, t) -> ctx.onEnd());
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
    public InstrumentationContext<ExecutionResult> beginExecutionStrategy(InstrumentationExecutionStrategyParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginField(InstrumentationFieldParameters parameters) {
        return new SimpleInstrumentationContext<>();
    }

}

package com.example.saga.common;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OrderSagaWorkflow {
    @WorkflowMethod
    void execute(OrderSagaInput input);
}

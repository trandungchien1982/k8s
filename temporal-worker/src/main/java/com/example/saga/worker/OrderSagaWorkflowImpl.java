package com.example.saga.worker;
import com.example.saga.common.OrderSagaInput;
import com.example.saga.common.OrderSagaWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Saga;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class OrderSagaWorkflowImpl implements OrderSagaWorkflow {
 private final SagaActivities activities = Workflow.newActivityStub(SagaActivities.class,
   ActivityOptions.newBuilder()
     .setStartToCloseTimeout(Duration.ofSeconds(10))
     .setRetryOptions(RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(1)).setBackoffCoefficient(2.0).setMaximumAttempts(3).build())
     .build());

 @Override public void execute(OrderSagaInput input) {
   Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
   try {
     activities.updateOrder(input.orderId(),"IN_PROGRESS","");
     activities.reserveInventory(input); saga.addCompensation(activities::releaseInventory,input.orderId());
     activities.chargePayment(input); saga.addCompensation(activities::refundPayment,input.orderId());
     activities.createShipment(input); saga.addCompensation(activities::cancelShipment,input.orderId());
     activities.updateOrder(input.orderId(),"COMPLETED","");
   } catch (Exception e) {
     saga.compensate();
     activities.updateOrder(input.orderId(),"FAILED",safeMessage(e));
   }
 }
 private String safeMessage(Exception e){ String m=e.getMessage(); if(m==null) return e.getClass().getSimpleName(); return m.length()>500?m.substring(0,500):m; }
}

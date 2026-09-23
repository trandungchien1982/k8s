package com.example.saga.worker;
import com.example.saga.common.SagaConstants;
import io.temporal.client.WorkflowClient; import io.temporal.serviceclient.*; import io.temporal.worker.*;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*;

@Configuration
public class TemporalWorkerConfig {
 @Bean(destroyMethod="shutdown") WorkflowServiceStubs service(@Value("${temporal.target:temporal:7233}") String target){return WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());}
 @Bean WorkflowClient workflowClient(WorkflowServiceStubs s){return WorkflowClient.newInstance(s);}
 @Bean(initMethod="start",destroyMethod="shutdown") WorkerFactory workerFactory(WorkflowClient client,SagaActivitiesImpl activities){
   WorkerFactory factory=WorkerFactory.newInstance(client); Worker worker=factory.newWorker(SagaConstants.TASK_QUEUE);
   worker.registerWorkflowImplementationTypes(OrderSagaWorkflowImpl.class); worker.registerActivitiesImplementations(activities); return factory;
 }
}

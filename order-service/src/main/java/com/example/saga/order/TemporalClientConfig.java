package com.example.saga.order;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalClientConfig {
    @Bean(destroyMethod="shutdown")
    WorkflowServiceStubs workflowServiceStubs(@Value("${temporal.target:temporal:7233}") String target) {
        return WorkflowServiceStubs.newServiceStubs(
                io.temporal.serviceclient.WorkflowServiceStubsOptions.newBuilder().setTarget(target).build());
    }
    @Bean WorkflowClient workflowClient(WorkflowServiceStubs service) { return WorkflowClient.newInstance(service); }
}

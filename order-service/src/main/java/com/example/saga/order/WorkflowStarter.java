package com.example.saga.order;

import com.example.saga.common.OrderSagaInput;
import com.example.saga.common.OrderSagaWorkflow;
import com.example.saga.common.SagaConstants;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionAlreadyStarted;
import io.temporal.client.WorkflowOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class WorkflowStarter {
    private static final Logger log = LoggerFactory.getLogger(WorkflowStarter.class);
    private final WorkflowStartRequestRepository requests;
    private final WorkflowClient workflowClient;

    public WorkflowStarter(WorkflowStartRequestRepository requests, WorkflowClient workflowClient) {
        this.requests = requests;
        this.workflowClient = workflowClient;
    }

    @Scheduled(fixedDelayString = "${workflow-start.publish-delay-ms:500}")
    @Transactional
    public void startPendingWorkflows() {
        for (WorkflowStartRequestEntity request : requests.findTop20ByStartedFalseOrderByIdAsc()) {
            OrderSagaWorkflow saga = workflowClient.newWorkflowStub(
                    OrderSagaWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setWorkflowId(request.getWorkflowId())
                            .setTaskQueue(SagaConstants.TASK_QUEUE)
                            .build());
            try {
                WorkflowClient.start(saga::execute, new OrderSagaInput(
                        request.getOrderId(), request.getProductId(), request.getQuantity(), request.getAmount(),
                        request.isFailPayment(), request.isFailShipping()));
                request.markStarted();
                log.info("Started Temporal workflow {}", request.getWorkflowId());
            } catch (WorkflowExecutionAlreadyStarted alreadyStarted) {
                // Fixed workflowId makes re-dispatch safe after a pod crash between Temporal start and DB commit.
                request.markStarted();
                log.info("Workflow {} was already started; marking dispatch complete", request.getWorkflowId());
            }
        }
    }
}

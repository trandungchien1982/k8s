package com.example.saga.order;

import com.example.saga.common.OutboxEvent;
import com.example.saga.common.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class OrderCreationService {
    private final OrderRepository orders;
    private final OutboxEventRepository outbox;
    private final ObjectMapper mapper;
    private final WorkflowStartRequestRepository workflowStarts;

    public OrderCreationService(OrderRepository orders, OutboxEventRepository outbox, ObjectMapper mapper, WorkflowStartRequestRepository workflowStarts) {
        this.orders = orders;
        this.outbox = outbox;
        this.mapper = mapper;
        this.workflowStarts = workflowStarts;
    }

    @Transactional
    public OrderEntity create(String productId, int quantity, BigDecimal amount, boolean failPayment, boolean failShipping) {
        OrderEntity order = orders.save(new OrderEntity(productId, quantity, amount));
        workflowStarts.save(new WorkflowStartRequestEntity("order-saga-" + order.getId(), order.getId(), productId, quantity, amount, failPayment, failShipping));
        try {
            outbox.save(new OutboxEvent(
                    "ORDER",
                    order.getId().toString(),
                    "ORDER_CREATED",
                    mapper.writeValueAsString(Map.of("type", "ORDER_CREATED", "orderId", order.getId()))));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not serialize ORDER_CREATED event", e);
        }
        return order;
    }
}

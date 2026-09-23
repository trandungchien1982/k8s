package com.example.saga.order;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "workflow_start_request", uniqueConstraints = @UniqueConstraint(columnNames = "workflowId"))
public class WorkflowStartRequestEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String workflowId;
    @Column(nullable = false)
    private Long orderId;
    private String productId;
    private int quantity;
    private BigDecimal amount;
    private boolean failPayment;
    private boolean failShipping;
    private boolean started;

    protected WorkflowStartRequestEntity() {}

    public WorkflowStartRequestEntity(String workflowId, Long orderId, String productId, int quantity, BigDecimal amount,
                                      boolean failPayment, boolean failShipping) {
        this.workflowId = workflowId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.failPayment = failPayment;
        this.failShipping = failShipping;
        this.started = false;
    }

    public Long getId() { return id; }
    public String getWorkflowId() { return workflowId; }
    public Long getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public boolean isFailPayment() { return failPayment; }
    public boolean isFailShipping() { return failShipping; }
    public boolean isStarted() { return started; }
    public void markStarted() { this.started = true; }
}

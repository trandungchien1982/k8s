package com.example.saga.order;

import com.example.saga.common.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orders;
    private final OrderCreationService creationService;
    private final OutboxEventRepository outbox;
    private final ObjectMapper mapper;

    public OrderController(OrderRepository orders, OrderCreationService creationService, OutboxEventRepository outbox, ObjectMapper mapper) {
        this.orders=orders; this.creationService=creationService; this.outbox=outbox; this.mapper=mapper;
    }

    public record CreateOrderRequest(String productId, int quantity, BigDecimal amount, boolean failPayment, boolean failShipping) {}

    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@RequestBody CreateOrderRequest req) throws Exception {
        OrderEntity order = creationService.create(req.productId(), req.quantity(), req.amount(), req.failPayment(), req.failShipping());
        String workflowId = "order-saga-" + order.getId();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("orderId",order.getId(),"workflowId",workflowId,"status",order.getStatus()));
    }

    @GetMapping("/{id}")
    public OrderEntity get(@PathVariable Long id) { return orders.findById(id).orElseThrow(); }

    @PatchMapping("/{id}/status")
    @Transactional
    public OrderEntity updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest req) throws Exception {
        OrderEntity order=orders.findById(id).orElseThrow();
        order.updateStatus(req.status(), req.reason());
        outbox.save(new OutboxEvent("ORDER", id.toString(), "ORDER_STATUS_CHANGED",
                mapper.writeValueAsString(Map.of("type","ORDER_STATUS_CHANGED","orderId",id,"status",req.status(),"reason",req.reason()==null?"":req.reason()))));
        return order;
    }
}

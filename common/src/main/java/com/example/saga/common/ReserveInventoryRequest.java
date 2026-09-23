package com.example.saga.common;
public record ReserveInventoryRequest(Long orderId, String productId, int quantity) {}

package com.example.saga.common;
public record ShippingRequest(Long orderId, String productId, int quantity, boolean fail) {}

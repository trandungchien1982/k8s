package com.example.saga.common;
import java.math.BigDecimal;
public record PaymentRequest(Long orderId, BigDecimal amount, boolean fail) {}

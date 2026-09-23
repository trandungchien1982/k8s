package com.example.saga.common;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderSagaInput(
        Long orderId,
        String productId,
        int quantity,
        BigDecimal amount,
        boolean failPayment,
        boolean failShipping
) implements Serializable {}

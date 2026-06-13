package com.glow.order.infrastructure.clients;

import java.util.UUID;

public record PaymentIntentRequest(
    UUID orderId,
    UUID customerId,
    Integer amount
) {
}
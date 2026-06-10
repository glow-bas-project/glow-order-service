package com.glow.order.infrastructure.clients;

import java.util.UUID;

public record PaymentIntentResponse(
    UUID id,
    String stripePaymentIntentId,
    Integer amount,
    UUID customerId,
    UUID orderId,
    String status
) {
}
package com.glow.order.application.api.model;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.OrderStatus;

public record UpdateOrderRequest(
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    Integer totalPrice,
    String stripePaymentIntentId,
    String transferGroup,
    Integer platformFeeAmount,
    String restaurantTransferId,
    String courierTransferId,
    OrderStatus status
) {
}
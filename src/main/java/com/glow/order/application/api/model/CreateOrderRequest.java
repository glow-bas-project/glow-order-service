package com.glow.order.application.api.model;

import com.glow.order.domain.model.Address;

public record CreateOrderRequest(
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    Integer totalPrice,
    String stripePaymentIntentId
    ) {
}

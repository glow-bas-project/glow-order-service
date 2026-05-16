package com.glow.order.application.model;

import com.glow.order.domain.model.Address;

public record OrderDto(
    String id,
    Integer totalPrice,
    String stripePaymentIntentId,
    String transferGroup,
    Integer platformFeeAmount,
    String status,
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    String restaurantTransferId,
    String courierTransferId
) {
}
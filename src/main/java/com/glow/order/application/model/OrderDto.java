package com.glow.order.application.model;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.OrderItem;

import java.util.List;

public record OrderDto(
    String id,
    Integer totalPrice,
    String stripePaymentIntentId,
    String stripeClientSecret,
    String transferGroup,
    Integer platformFeeAmount,
    String status,
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    String restaurantTransferId,
    String courierTransferId,
    List<OrderItem> orderItems
) {
}
package com.glow.order.application.api.model;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.OrderItem;

import java.util.List;

public record CreateOrderRequest(
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    Integer totalPrice,
    String stripePaymentIntentId,
    String transferGroup,
    List<OrderItem> orderItems
    ) {
}

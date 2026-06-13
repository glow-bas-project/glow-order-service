package com.glow.order.application.api.model;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public record CheckoutOrderRequest(
    Address deliveryAddress,
    Address restaurantAddress,
    String phoneNumber,
    UUID customerId,
    Integer totalPrice,
    UUID restaurantId, 
    List<OrderItem> orderItems
) {
}
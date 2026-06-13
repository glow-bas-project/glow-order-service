package com.glow.order.infrastructure.clients;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public record RestaurantNotificationRequest(
    UUID orderId,
    Address deliveryAddress,
    String customerPhone,
    List<OrderItem> items,
    Integer totalPrice
) {}
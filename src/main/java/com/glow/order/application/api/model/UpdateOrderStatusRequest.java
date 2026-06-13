package com.glow.order.application.api.model;

import com.glow.order.domain.model.OrderStatus;

public record UpdateOrderStatusRequest(OrderStatus status) {}
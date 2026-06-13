package com.glow.order.domain.model;

import java.util.UUID;

public record OrderItem(
    UUID menuItemId,
    String name,
    Integer price,
    Integer quantity
) {}
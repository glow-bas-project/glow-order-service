package com.glow.order.application.api.model;

import java.util.UUID;

public record NotifyRestaurantRequest(
    UUID restaurantId
) {}
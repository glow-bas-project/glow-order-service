package com.glow.order.application.api.model;

public record FindOrderIdsRequest(
    int size,
    int page
) {
}

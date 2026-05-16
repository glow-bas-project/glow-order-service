package com.glow.order.application.api.model;

import java.time.Instant;

public record ApiError(
    String code,
    String message,
    int status,
    Instant timestamp) {
}

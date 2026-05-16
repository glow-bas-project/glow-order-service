package com.glow.order.application.api.model;

import java.util.List;

public record MaterialiseOrdersByIdsRequest(
    List<String> ids
) {
}
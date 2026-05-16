package com.glow.order.application.api.model;

import java.util.List;

public record FindOrderIdsResponse(
    List<String> ids,
    int nextPage,
    boolean hasNext
) {
}

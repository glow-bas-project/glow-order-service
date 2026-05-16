package com.glow.order.domain.shared;

import java.util.List;

public record PageResult<T>(
    List<T> result,
    int nextPage,
    boolean lastPage) {

    public int size() {
        return result.size();
    }
}

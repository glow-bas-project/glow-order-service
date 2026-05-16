package com.glow.order.domain.shared;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

public class DomainPrecondition {

    public static <T> T requireNonNull(T value, String errorMessage) {
        if (value == null) {
            throw new DomainException(errorMessage);
        }

        return value;
    }

    public static String requireNonBlank(String value, String errorMessage) {
        if (StringUtils.isEmpty(value)) {
            throw new DomainException(errorMessage);
        }

        return value;
    }

    public static <T> List<T> requireNonEmpty(List<T> values, String errorMessage) {
        if (CollectionUtils.isEmpty(values)) {
            throw new DomainException(errorMessage);
        }

        return values;
    }

    public static <T> Set<T> requireNonEmpty(Set<T> values, String errorMessage) {
        if (CollectionUtils.isEmpty(values)) {
            throw new DomainException(errorMessage);
        }

        return values;
    }
}

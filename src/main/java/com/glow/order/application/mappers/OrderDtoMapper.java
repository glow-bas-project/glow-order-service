package com.glow.order.application.mappers;

import com.glow.order.application.model.OrderDto;
import com.glow.order.domain.model.Order;
import com.glow.order.domain.model.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "cdi")
public interface OrderDtoMapper {

    OrderDto toDto(Order order);
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toDomain(OrderDto dto);

    default String map(UUID id) {
        return id == null ? null : id.toString();
    }

    default UUID map(String id) {
        return id == null || id.isBlank() ? null : UUID.fromString(id);
    }

    default String map(OrderStatus status) {
        return status == null ? null : status.name();
    }

    default OrderStatus mapStatus(String status) {
        return status == null || status.isBlank() ? null : OrderStatus.valueOf(status);
    }
}

package com.glow.order.infrastructure.mappers;

import com.glow.order.domain.model.Order;
import com.glow.order.infrastructure.repository.entities.OrderJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface OrderEntityMapper {

    OrderJpaEntity toEntity(Order order);
    Order toDomain(OrderJpaEntity entity);
}
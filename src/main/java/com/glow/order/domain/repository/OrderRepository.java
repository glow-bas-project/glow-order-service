package com.glow.order.domain.repository;

import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import com.glow.order.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    void update(Order order);

    Optional<Order> findById(String id);

    Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId);

    PageResult<String> findIds(PageRequest pageRequest);

    List<Order> findByIds(List<String> ids);

    boolean deleteById(String id);
}
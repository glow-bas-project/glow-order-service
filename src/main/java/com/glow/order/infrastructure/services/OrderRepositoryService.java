package com.glow.order.infrastructure.services;

import com.glow.order.domain.model.Order;
import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import com.glow.order.domain.repository.OrderRepository;
import com.glow.order.infrastructure.mappers.OrderEntityMapper;
import com.glow.order.infrastructure.repository.OrderJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderRepositoryService implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderEntityMapper mapper;

    public OrderRepositoryService(OrderJpaRepository jpaRepository, OrderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void save(Order order) {
        jpaRepository.persist(mapper.toEntity(order));
    }

    @Override
    @Transactional
    public void update(Order order) {
        jpaRepository.getEntityManager().merge(mapper.toEntity(order));
    }

    @Override
    @Transactional
    public Optional<Order> findById(String id) {
        return jpaRepository.findByIdOptional(id)
            .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return jpaRepository.findByStripePaymentIntentId(stripePaymentIntentId)
            .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public PageResult<String> findIds(PageRequest pageRequest) {
        return jpaRepository.findIds(pageRequest);
    }

    @Override
    @Transactional
    public List<Order> findByIds(List<String> ids) {
        return jpaRepository.findOrdersByIds(ids).stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        return jpaRepository.deleteById(id);
    }
}

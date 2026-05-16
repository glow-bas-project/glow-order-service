package com.glow.order.infrastructure.repository;

import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import com.glow.order.infrastructure.repository.entities.OrderJpaEntity;
import io.quarkus.panache.common.Page;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderJpaRepository implements PanacheRepositoryBase<OrderJpaEntity, String> {

    public Optional<OrderJpaEntity> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return find("stripePaymentIntentId", stripePaymentIntentId).firstResultOptional();
    }

    public PageResult<String> findIds(PageRequest pageRequest) {
        var query = findAll().page(Page.of(pageRequest.page(), pageRequest.size()));
        var hasNext = query.hasNextPage();

        return new PageResult<>(
            query.list().stream().map(OrderJpaEntity::getId).toList(),
            hasNext ? pageRequest.page() + 1 : pageRequest.page(),
            !hasNext);
    }

    public List<OrderJpaEntity> findOrdersByIds(List<String> ids) {
        return list("id in ?1", ids);
    }
}

package com.glow.order.infrastructure.services;

import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.Order;
import com.glow.order.domain.model.OrderStatus;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class OrderRepositoryServiceIntegrationTest {

    @Inject
    OrderRepositoryService repositoryService;

    @Test
    void saveAndFindById_roundTripsOrder() {
        // given
        var order = createOrder("pi_save_and_find");

        // when
        repositoryService.save(order);

        var actual = repositoryService.findById(order.getId().toString()).orElseThrow();

        // then
        assertEquals(order.getId(), actual.getId());
        assertEquals(order.getStripePaymentIntentId(), actual.getStripePaymentIntentId());
        assertEquals(order.getTotalPrice(), actual.getTotalPrice());
        assertEquals(order.getStatus(), actual.getStatus());
        assertEquals(order.getDeliveryAddress(), actual.getDeliveryAddress());
        assertEquals(order.getRestaurantAddress(), actual.getRestaurantAddress());
        repositoryService.deleteById(order.getId().toString());
    }

    @Test
    void findByStripePaymentIntentId_returnsSavedOrder() {
        // given
        var order = createOrder("pi_lookup");

        // when
        repositoryService.save(order);

        var actual = repositoryService.findByStripePaymentIntentId("pi_lookup").orElseThrow();

        // then
        assertEquals(order.getId(), actual.getId());
        assertEquals(order.getStripePaymentIntentId(), actual.getStripePaymentIntentId());
        assertEquals(order.getStatus(), actual.getStatus());
        repositoryService.deleteById(order.getId().toString());
    }

    @Test
    void findIds_returnsSavedId() {
        // given
        var order = createOrder("pi_list");

        // when
        repositoryService.save(order);

        var pageResult = repositoryService.findIds(new com.glow.order.domain.shared.PageRequest(10, 0));

        // then
        assertTrue(pageResult.result().contains(order.getId().toString()));
        assertTrue(pageResult.lastPage());
        repositoryService.deleteById(order.getId().toString());
    }

    @Test
    void deleteById_removesOrder() {
        // given
        var order = createOrder("pi_delete");

        // when
        repositoryService.save(order);
        assertTrue(repositoryService.deleteById(order.getId().toString()));

        // then
        assertFalse(repositoryService.findById(order.getId().toString()).isPresent());
    }

    private static Order createOrder(String paymentIntentId) {
        return Order.builder()
            .id(UUID.randomUUID())
            .phoneNumber("12345678")
            .deliveryAddress(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .restaurantAddress(new Address("2 Main St", "City", "Country", 3.0, 4.0))
            .stripePaymentIntentId(paymentIntentId)
            .totalPrice(1500)
            .status(OrderStatus.CREATED)
            .build();
    }
}
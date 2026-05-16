package com.glow.order.domain.model;

import com.glow.order.domain.shared.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void buildAppliesDefaults() {
        // given
        var order = Order.builder()
            .phoneNumber("12345678")
            .deliveryAddress(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .restaurantAddress(new Address("2 Main St", "City", "Country", 3.0, 4.0))
            .stripePaymentIntentId("pi_test_123")
            .build();

        // then
        assertNotNull(order.getId());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(0, order.getPlatformFeeAmount());
        assertTrue(order.getTransferGroup() != null && !order.getTransferGroup().isBlank());
    }

    @Test
    void buildThrowsWhenStripePaymentIntentIdMissing() {
        // given / when / then
        assertThrows(DomainException.class, () -> Order.builder()
            .phoneNumber("12345678")
            .deliveryAddress(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .restaurantAddress(new Address("2 Main St", "City", "Country", 3.0, 4.0))
            .build());
    }

    @Test
    void toBuilderProducesEquivalentOrder() {
        // given
        var order = Order.builder()
            .phoneNumber("12345678")
            .deliveryAddress(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .restaurantAddress(new Address("2 Main St", "City", "Country", 3.0, 4.0))
            .stripePaymentIntentId("pi_test_123")
            .status(OrderStatus.PROCESSING)
            .build();

        // when
        var copy = order.toBuilder().build();

        // then
        assertEquals(order, copy);
    }
}
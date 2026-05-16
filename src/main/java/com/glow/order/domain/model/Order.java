package com.glow.order.domain.model;

import com.glow.order.domain.shared.DomainPrecondition;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Integer totalPrice;
    private final String stripePaymentIntentId;
    private final String transferGroup;
    private final Integer platformFeeAmount;
    private final String restaurantTransferId;
    private final String courierTransferId;
    private final OrderStatus status;
    private final Address deliveryAddress;
    private final Address restaurantAddress;
    private final String phoneNumber;

    private Order(Builder builder) {
        if (Objects.isNull(builder.id)) {
            this.id = UUID.randomUUID();
        } else {
            this.id = builder.id;
        }

        if (Objects.isNull(builder.createdAt)) {
            this.createdAt = Instant.now();
        } else {
            this.createdAt = builder.createdAt;
        }

        if (Objects.isNull(builder.updatedAt)) {
            this.updatedAt = Instant.now();
        } else {
            this.updatedAt = builder.updatedAt;
        }

        if (Objects.isNull(builder.totalPrice)) {
            this.totalPrice = 0;
        } else {
            this.totalPrice = builder.totalPrice;
        }

        this.stripePaymentIntentId = DomainPrecondition.requireNonBlank(builder.stripePaymentIntentId,
            "Stripe payment intent ID cannot be null or empty");

        if (Objects.isNull(builder.transferGroup) || builder.transferGroup.isBlank()) {
            this.transferGroup = UUID.randomUUID().toString();
        } else {
            this.transferGroup = builder.transferGroup;
        }

        if (Objects.isNull(builder.platformFeeAmount)) {
            this.platformFeeAmount = 0;
        } else {
            this.platformFeeAmount = builder.platformFeeAmount;
        }

        this.restaurantTransferId = builder.restaurantTransferId;

        this.courierTransferId = builder.courierTransferId;

        if (Objects.isNull(builder.status)) {
            this.status = OrderStatus.CREATED;
        } else {
            this.status = builder.status;
        }

        this.deliveryAddress = DomainPrecondition.requireNonNull(builder.deliveryAddress,
            "Delivery address cannot be null");

        this.restaurantAddress = DomainPrecondition.requireNonNull(builder.restaurantAddress,
            "Restaurant address cannot be null");

        this.phoneNumber = DomainPrecondition.requireNonBlank(builder.phoneNumber,
            "Phone number cannot be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public String getTransferGroup() {
        return transferGroup;
    }

    public Integer getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public String getRestaurantTransferId() {
        return restaurantTransferId;
    }

    public String getCourierTransferId() {
        return courierTransferId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Address getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Builder toBuilder() {
        return new Builder()
            .id(id)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .totalPrice(totalPrice)
            .stripePaymentIntentId(stripePaymentIntentId)
            .transferGroup(transferGroup)
            .platformFeeAmount(platformFeeAmount)
            .restaurantTransferId(restaurantTransferId)
            .courierTransferId(courierTransferId)
            .status(status)
            .deliveryAddress(deliveryAddress)
            .restaurantAddress(restaurantAddress)
            .phoneNumber(phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(createdAt, order.createdAt) &&
            Objects.equals(updatedAt, order.updatedAt) && Objects.equals(totalPrice, order.totalPrice) &&
            Objects.equals(stripePaymentIntentId, order.stripePaymentIntentId) &&
            Objects.equals(transferGroup, order.transferGroup) &&
            Objects.equals(platformFeeAmount, order.platformFeeAmount) &&
            Objects.equals(restaurantTransferId, order.restaurantTransferId) &&
            Objects.equals(courierTransferId, order.courierTransferId) &&
            status == order.status && Objects.equals(deliveryAddress, order.deliveryAddress) &&
            Objects.equals(restaurantAddress, order.restaurantAddress) &&
            Objects.equals(phoneNumber, order.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, updatedAt, totalPrice, stripePaymentIntentId, transferGroup,
            platformFeeAmount, restaurantTransferId, courierTransferId, status, deliveryAddress,
            restaurantAddress, phoneNumber);
    }

    public static class Builder {
        private UUID id;
        private Instant createdAt;
        private Instant updatedAt;
        private Integer totalPrice;
        private String stripePaymentIntentId;
        private String transferGroup;
        private Integer platformFeeAmount;
        private String restaurantTransferId;
        private String courierTransferId;
        private OrderStatus status;
        private Address deliveryAddress;
        private Address restaurantAddress;
        private String phoneNumber;
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder totalPrice(Integer totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder stripePaymentIntentId(String stripePaymentIntentId) {
            this.stripePaymentIntentId = stripePaymentIntentId;
            return this;
        }

        public Builder transferGroup(String transferGroup) {
            this.transferGroup = transferGroup;
            return this;
        }

        public Builder platformFeeAmount(Integer platformFeeAmount) {
            this.platformFeeAmount = platformFeeAmount;
            return this;
        }

        public Builder restaurantTransferId(String restaurantTransferId) {
            this.restaurantTransferId = restaurantTransferId;
            return this;
        }

        public Builder courierTransferId(String courierTransferId) {
            this.courierTransferId = courierTransferId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder deliveryAddress(Address deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder restaurantAddress(Address restaurantAddress) {
            this.restaurantAddress = restaurantAddress;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
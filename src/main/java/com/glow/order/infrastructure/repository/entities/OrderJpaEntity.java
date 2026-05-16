package com.glow.order.infrastructure.repository.entities;

import com.glow.order.domain.model.OrderStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private String id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(name = "transfer_group")
    private String transferGroup;

    @Column(name = "platform_fee_amount")
    private Integer platformFeeAmount;

    @Column(name = "restaurant_transfer_id")
    private String restaurantTransferId;

    @Column(name = "courier_transfer_id")
    private String courierTransferId;

    @Embedded
    private RestaurantAddressEntity restaurantAddress;

    @Embedded
    private DeliveryAddressEntity deliveryAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStripePaymentIntentId() {
        return stripePaymentIntentId;
    }

    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public String getTransferGroup() {
        return transferGroup;
    }

    public void setTransferGroup(String transferGroup) {
        this.transferGroup = transferGroup;
    }

    public Integer getPlatformFeeAmount() {
        return platformFeeAmount;
    }

    public void setPlatformFeeAmount(Integer platformFeeAmount) {
        this.platformFeeAmount = platformFeeAmount;
    }

    public String getRestaurantTransferId() {
        return restaurantTransferId;
    }

    public void setRestaurantTransferId(String restaurantTransferId) {
        this.restaurantTransferId = restaurantTransferId;
    }

    public String getCourierTransferId() {
        return courierTransferId;
    }

    public void setCourierTransferId(String courierTransferId) {
        this.courierTransferId = courierTransferId;
    }

    public RestaurantAddressEntity getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(RestaurantAddressEntity restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public DeliveryAddressEntity getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressEntity deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

}
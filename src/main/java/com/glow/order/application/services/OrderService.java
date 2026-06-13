package com.glow.order.application.services;

import com.glow.order.application.api.model.CheckoutOrderRequest;
import com.glow.order.application.api.model.CreateOrderRequest;
import com.glow.order.application.api.model.UpdateOrderRequest;
import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import com.glow.order.application.mappers.OrderDtoMapper;
import com.glow.order.application.model.OrderDto;
import com.glow.order.domain.model.Order;
import com.glow.order.domain.model.OrderItem;
import com.glow.order.domain.model.OrderStatus;
import com.glow.order.domain.repository.OrderRepository;
import com.glow.order.domain.shared.DomainPrecondition;
import com.glow.order.infrastructure.clients.PaymentApiClient;
import com.glow.order.infrastructure.clients.PaymentIntentRequest;
import com.glow.order.infrastructure.clients.RestaurantApiClient;
import com.glow.order.infrastructure.clients.RestaurantNotificationRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderService {

    private static final Logger LOG = Logger.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderDtoMapper mapper;
    private final PaymentApiClient paymentApiClient;
    private final RestaurantApiClient restaurantApiClient;

    public OrderService(OrderRepository orderRepository, OrderDtoMapper mapper,
                    @RestClient PaymentApiClient paymentApiClient,
                    @RestClient RestaurantApiClient restaurantApiClient) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.paymentApiClient = paymentApiClient;
        this.restaurantApiClient = restaurantApiClient;
    }

    public OrderDto createOrder(CreateOrderRequest request) {
        var order = buildOrder(
            request.deliveryAddress(),
            request.restaurantAddress(),
            request.phoneNumber(),
            request.totalPrice(),
            request.stripePaymentIntentId(),
            null,
            OrderStatus.CREATED,
            request.orderItems());

        orderRepository.save(order);
        return mapper.toDto(order);
    }

    public OrderDto checkoutOrder(CheckoutOrderRequest request) {
        DomainPrecondition.requireNonNull(request, "checkout request is required");
        DomainPrecondition.requireNonNull(request.customerId(), "customerId is required");
        DomainPrecondition.requireNonNull(request.totalPrice(), "totalPrice is required");
        if (request.totalPrice() <= 0) {
            throw new com.glow.order.domain.shared.DomainException("totalPrice must be greater than 0");
        }

        var draftOrder = buildOrder(
            request.deliveryAddress(),
            request.restaurantAddress(),
            request.phoneNumber(),
            request.totalPrice(),
            "pending-" + UUID.randomUUID(),
            null,
            OrderStatus.CREATED,
            request.orderItems());

        var paymentIntent = paymentApiClient.createPaymentIntent(
            new PaymentIntentRequest(draftOrder.getId(), request.customerId(), request.totalPrice()));

        var order = draftOrder.toBuilder()
            .stripePaymentIntentId(paymentIntent.stripePaymentIntentId())
            .stripeClientSecret(paymentIntent.stripeClientSecret())
            .status(OrderStatus.PROCESSING)
            .build();

        orderRepository.save(order);

        if (request.restaurantId() != null) {
            try {
                restaurantApiClient.notifyRestaurant(
                    request.restaurantId(),
                    new RestaurantNotificationRequest(
                        order.getId(),
                        order.getDeliveryAddress(),
                        order.getPhoneNumber(),
                        order.getOrderItems(),
                        order.getTotalPrice()
                    )
                );
                LOG.infof("Restaurant %s notified of order %s", request.restaurantId(), order.getId());
            } catch (Exception e) {
                // Log but don't fail the checkout — order is already saved and payment intent created
                LOG.warnf("Failed to notify restaurant %s: %s", request.restaurantId(), e.getMessage());
            }
        }

        return mapper.toDto(order);
    }

    public OrderDto findById(String id) {
        return mapper.toDto(
            orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found")));
    }

    public OrderDto findByStripePaymentIntentId(String stripePaymentIntentId) {
        return mapper.toDto(
            orderRepository.findByStripePaymentIntentId(stripePaymentIntentId)
                .orElseThrow(() -> new NotFoundException("Order with stripe payment intent id " + stripePaymentIntentId + " not found")));
    }

    public PageResult<String> findOrderIds(int size, int page) {
        // TODO: Build some defaults but for now this is fine
        return orderRepository.findIds(new PageRequest(size, page));
    }

    public List<OrderDto> materialise(List<String> ids) {
        return orderRepository.findByIds(ids).stream()
            .map(mapper::toDto)
            .toList();
    }

    public OrderDto updateOrder(String id, UpdateOrderRequest request) {
        var existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Order with id " + id + " not found"));

        var updatedOrder = existingOrder.toBuilder()
            .deliveryAddress(request.deliveryAddress() != null ? request.deliveryAddress() : existingOrder.getDeliveryAddress())
            .restaurantAddress(request.restaurantAddress() != null ? request.restaurantAddress() : existingOrder.getRestaurantAddress())
            .phoneNumber(request.phoneNumber() != null ? request.phoneNumber() : existingOrder.getPhoneNumber())
            .totalPrice(request.totalPrice() != null ? request.totalPrice() : existingOrder.getTotalPrice())
            .stripePaymentIntentId(request.stripePaymentIntentId() != null ? request.stripePaymentIntentId() : existingOrder.getStripePaymentIntentId())
            .transferGroup(request.transferGroup() != null ? request.transferGroup() : existingOrder.getTransferGroup())
            .platformFeeAmount(request.platformFeeAmount() != null ? request.platformFeeAmount() : existingOrder.getPlatformFeeAmount())
            .restaurantTransferId(request.restaurantTransferId() != null ? request.restaurantTransferId() : existingOrder.getRestaurantTransferId())
            .courierTransferId(request.courierTransferId() != null ? request.courierTransferId() : existingOrder.getCourierTransferId())
            .status(request.status() != null ? request.status() : existingOrder.getStatus())
            .build();

        orderRepository.update(updatedOrder);
        return mapper.toDto(updatedOrder);
    }

    public void notifyRestaurant(String orderId, UUID restaurantId) {
        var order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        restaurantApiClient.notifyRestaurant(
            restaurantId,
            new RestaurantNotificationRequest(
                order.getId(),
                order.getDeliveryAddress(),
                order.getPhoneNumber(),
                order.getOrderItems(),
                order.getTotalPrice()
            )
        );
        LOG.infof("Restaurant %s notified of order %s", restaurantId, orderId);
    }

    public OrderDto updateStatus(String id, OrderStatus status) {
        var existing = orderRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Order not found: " + id));

        var updated = existing.toBuilder()
            .status(status)
            .build();

        orderRepository.update(updated);
        LOG.infof("Order %s status updated to %s", id, status);
        return mapper.toDto(updated);
    }

    public boolean deleteOrderById(String id) {
        return orderRepository.deleteById(id);
    }

    private Order buildOrder(
        com.glow.order.domain.model.Address deliveryAddress,
        com.glow.order.domain.model.Address restaurantAddress,
        String phoneNumber,
        Integer totalPrice,
        String stripePaymentIntentId,
        String stripeClientSecret,
        OrderStatus status,
        List<OrderItem> orderItems) {

        return Order.builder()
            .phoneNumber(phoneNumber)
            .deliveryAddress(deliveryAddress)
            .restaurantAddress(restaurantAddress)
            .stripePaymentIntentId(stripePaymentIntentId)
            .stripeClientSecret(stripeClientSecret)
            .totalPrice(totalPrice)
            .status(status)
            .orderItems(orderItems)
            .build();
    }
}

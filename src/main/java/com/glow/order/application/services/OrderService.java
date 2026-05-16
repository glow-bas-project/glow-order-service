package com.glow.order.application.services;

import com.glow.order.application.api.model.CreateOrderRequest;
import com.glow.order.application.api.model.UpdateOrderRequest;
import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import com.glow.order.application.mappers.OrderDtoMapper;
import com.glow.order.application.model.OrderDto;
import com.glow.order.domain.model.Order;
import com.glow.order.domain.model.OrderStatus;
import com.glow.order.domain.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoMapper mapper;

    public OrderService(OrderRepository orderRepository, OrderDtoMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public OrderDto createOrder(CreateOrderRequest request) {
        var order = Order.builder()
            .phoneNumber(request.phoneNumber())
            .deliveryAddress(request.deliveryAddress())
            .restaurantAddress(request.restaurantAddress())
            .stripePaymentIntentId(request.stripePaymentIntentId())
            .totalPrice(request.totalPrice())
            .status(OrderStatus.CREATED)
            .build();

        orderRepository.save(order);
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

    public boolean deleteOrderById(String id) {
        return orderRepository.deleteById(id);
    }
}

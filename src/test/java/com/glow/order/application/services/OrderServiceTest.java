package com.glow.order.application.services;

import com.glow.order.application.api.model.CreateOrderRequest;
import com.glow.order.application.api.model.UpdateOrderRequest;
import com.glow.order.application.mappers.OrderDtoMapper;
import com.glow.order.application.model.OrderDto;
import com.glow.order.domain.model.Address;
import com.glow.order.domain.model.Order;
import com.glow.order.domain.model.OrderStatus;
import com.glow.order.domain.repository.OrderRepository;
import com.glow.order.domain.shared.PageRequest;
import com.glow.order.domain.shared.PageResult;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderDtoMapper mapper;

    @InjectMocks
    OrderService service;

    @Test
    void createOrder_savesAndMapsToDto() {
        // given
        var request = new CreateOrderRequest(
            new Address("1 Main St", "City", "Country", 1.0, 2.0),
            new Address("2 Main St", "City", "Country", 3.0, 4.0),
            "12345678",
            1500,
            "pi_test_123");

        when(mapper.toDto(any(Order.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        // when
        var dto = service.createOrder(request);

        // then
        assertEquals("pi_test_123", dto.stripePaymentIntentId());
        assertEquals(1500, dto.totalPrice());
        assertEquals(OrderStatus.CREATED.name(), dto.status());
        verify(orderRepository).save(any(Order.class));
        verify(mapper).toDto(any(Order.class));
    }

    @Test
    void createOrder_appliesDefaultStatusAndTransferGroup() {
        // given
        var request = new CreateOrderRequest(
            new Address("1 Main St", "City", "Country", 1.0, 2.0),
            new Address("2 Main St", "City", "Country", 3.0, 4.0),
            "12345678",
            1500,
            "pi_test_123");

        when(mapper.toDto(any(Order.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        // when
        var captor = ArgumentCaptor.forClass(Order.class);
        service.createOrder(request);

        // then
        verify(orderRepository).save(captor.capture());
        assertEquals(OrderStatus.CREATED, captor.getValue().getStatus());
        assertNotNull(captor.getValue().getTransferGroup());
        assertFalse(captor.getValue().getTransferGroup().isBlank());
    }

    @Test
    void findById_throwsWhenMissing() {
        // given
        when(orderRepository.findById("missing")).thenReturn(Optional.empty());

        // when / then
        assertThrows(NotFoundException.class, () -> service.findById("missing"));
    }

    @Test
    void findById_returnsDtoWhenPresent() {
        // given
        var order = createOrder("order-1");
        var expected = toDto(order);

        when(orderRepository.findById(expected.id())).thenReturn(Optional.of(order));
        when(mapper.toDto(order)).thenReturn(expected);

        // when
        var actual = service.findById(expected.id());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findByStripePaymentIntentId_returnsDtoWhenPresent() {
        // given
        var order = createOrder("order-2");
        var expected = toDto(order);

        when(orderRepository.findByStripePaymentIntentId(order.getStripePaymentIntentId())).thenReturn(Optional.of(order));
        when(mapper.toDto(order)).thenReturn(expected);

        // when
        var actual = service.findByStripePaymentIntentId(order.getStripePaymentIntentId());

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findOrderIds_delegatesToRepository() {
        // given
        var pageResult = new PageResult<>(List.of("order-1"), 1, true);
        when(orderRepository.findIds(any(PageRequest.class))).thenReturn(pageResult);

        // when
        var actual = service.findOrderIds(10, 0);

        // then
        assertEquals(pageResult, actual);
        var captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(orderRepository).findIds(captor.capture());
        assertEquals(10, captor.getValue().size());
        assertEquals(0, captor.getValue().page());
    }

    @Test
    void materialise_mapsEachOrder() {
        // given
        var order = createOrder("order-3");
        var dto = toDto(order);

        when(orderRepository.findByIds(List.of(order.getId().toString()))).thenReturn(List.of(order));
        when(mapper.toDto(order)).thenReturn(dto);

        // when
        var actual = service.materialise(List.of(order.getId().toString()));

        // then
        assertEquals(List.of(dto), actual);
    }

    @Test
    void updateOrder_updatesAndReturnsDto() {
        // given
        var existing = createOrder("order-4");
        var request = new UpdateOrderRequest(
            new Address("3 Main St", "New City", "Country", 5.0, 6.0),
            existing.getRestaurantAddress(),
            "99999999",
            2500,
            existing.getStripePaymentIntentId(),
            "transfer-group-updated",
            123,
            "restaurant-transfer-updated",
            "courier-transfer-updated",
            OrderStatus.PROCESSING);

        when(orderRepository.findById(existing.getId().toString())).thenReturn(Optional.of(existing));
        when(mapper.toDto(any(Order.class))).thenAnswer(invocation -> toDto(invocation.getArgument(0)));

        // when
        var dto = service.updateOrder(existing.getId().toString(), request);

        // then
        assertEquals(OrderStatus.PROCESSING.name(), dto.status());
        assertEquals("99999999", dto.phoneNumber());
        verify(orderRepository).update(any(Order.class));
    }

    @Test
    void deleteOrderById_returnsTrueWhenRepositoryDeletes() {
        // given
        when(orderRepository.deleteById("x")).thenReturn(true);

        // when / then
        assertTrue(service.deleteOrderById("x"));
    }

    @Test
    void deleteOrderById_returnsFalseWhenRepositoryDoesNotDelete() {
        // given
        when(orderRepository.deleteById("y")).thenReturn(false);

        // when / then
        assertFalse(service.deleteOrderById("y"));
    }

    private static Order createOrder(String id) {
        return Order.builder()
            .id(java.util.UUID.fromString(java.util.UUID.nameUUIDFromBytes(id.getBytes()).toString()))
            .phoneNumber("12345678")
            .deliveryAddress(new Address("1 Main St", "City", "Country", 1.0, 2.0))
            .restaurantAddress(new Address("2 Main St", "City", "Country", 3.0, 4.0))
            .stripePaymentIntentId("pi_" + id)
            .totalPrice(1500)
            .status(OrderStatus.CREATED)
            .build();
    }

    private static OrderDto toDto(Order order) {
        return new OrderDto(
            order.getId().toString(),
            order.getTotalPrice(),
            order.getStripePaymentIntentId(),
            order.getTransferGroup(),
            order.getPlatformFeeAmount(),
            order.getStatus().name(),
            order.getDeliveryAddress(),
            order.getRestaurantAddress(),
            order.getPhoneNumber(),
            order.getRestaurantTransferId(),
            order.getCourierTransferId());
    }
}
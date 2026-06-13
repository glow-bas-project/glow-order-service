package com.glow.order.application.api;

import com.glow.order.application.api.model.CheckoutOrderRequest;
import com.glow.order.application.api.model.CreateOrderRequest;
import com.glow.order.application.api.model.FindOrderIdsRequest;
import com.glow.order.application.api.model.FindOrderIdsResponse;
import com.glow.order.application.api.model.MaterialiseOrdersByIdsRequest;
import com.glow.order.application.api.model.UpdateOrderRequest;
import com.glow.order.application.api.model.NotifyRestaurantRequest;
import com.glow.order.application.api.model.UpdateOrderStatusRequest;
import com.glow.order.application.model.OrderDto;
import com.glow.order.application.services.OrderService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @POST
    @Path("create")
    public RestResponse<OrderDto> createOrder(CreateOrderRequest request) {
        return RestResponse.ok(service.createOrder(request));
    }

    @POST
    @Path("checkout")
    public RestResponse<OrderDto> checkoutOrder(CheckoutOrderRequest request) {
        return RestResponse.status(RestResponse.Status.CREATED, service.checkoutOrder(request));
    }

    @GET
    @Path("{id}")
    public RestResponse<OrderDto> findById(@PathParam("id") String id) {
        return RestResponse.ok(service.findById(id));
    }

    @GET
    @Path("stripe/{stripePaymentIntentId}")
    public RestResponse<OrderDto> findByStripePaymentIntentId(@PathParam("stripePaymentIntentId") String stripePaymentIntentId) {
        return RestResponse.ok(service.findByStripePaymentIntentId(stripePaymentIntentId));
    }

    @POST
    @Path("find")
    public RestResponse<FindOrderIdsResponse> findOrders(FindOrderIdsRequest request) {
        var result = service.findOrderIds(request.size(), request.page());

        return RestResponse.ok(new FindOrderIdsResponse(
            result.result(),
            result.nextPage(),
            !result.lastPage()));
    }

    @POST
    @Path("materialise")
    public RestResponse<java.util.List<OrderDto>> materialiseOrders(MaterialiseOrdersByIdsRequest request) {
        return RestResponse.ok(service.materialise(request.ids()));
    }

    @POST
    @Path("{id}/notify-restaurant")
    public RestResponse<Void> notifyRestaurant(@PathParam("id") String orderId, NotifyRestaurantRequest request) {
        service.notifyRestaurant(orderId, request.restaurantId());
        return RestResponse.noContent();
    }

    @PUT
    @Path("{id}")
    public RestResponse<OrderDto> updateById(@PathParam("id") String id, UpdateOrderRequest request) {
        return RestResponse.ok(service.updateOrder(id, request));
    }

    @PATCH
    @Path("{id}/status")
    public RestResponse<OrderDto> updateStatus(@PathParam("id") String id,
                                                UpdateOrderStatusRequest request) {
        return RestResponse.ok(service.updateStatus(id, request.status()));
    }

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        boolean deleted = service.deleteOrderById(id);
        return deleted ? RestResponse.noContent() : RestResponse.status(RestResponse.Status.NOT_FOUND);
    }
}
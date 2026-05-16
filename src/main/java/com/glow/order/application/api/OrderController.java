package com.glow.order.application.api;

import com.glow.order.application.api.model.CreateOrderRequest;
import com.glow.order.application.api.model.FindOrderIdsRequest;
import com.glow.order.application.api.model.FindOrderIdsResponse;
import com.glow.order.application.api.model.MaterialiseOrdersByIdsRequest;
import com.glow.order.application.api.model.UpdateOrderRequest;
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
    public RestResponse<OrderDto> createOrder(CreateOrderRequest request) {
        return RestResponse.ok(service.createOrder(request));
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

    @PUT
    @Path("{id}")
    public RestResponse<OrderDto> updateById(@PathParam("id") String id, UpdateOrderRequest request) {
        return RestResponse.ok(service.updateOrder(id, request));
    }

    @DELETE
    @Path("{id}")
    public RestResponse<Void> deleteById(@PathParam("id") String id) {
        boolean deleted = service.deleteOrderById(id);
        return deleted ? RestResponse.noContent() : RestResponse.status(RestResponse.Status.NOT_FOUND);
    }
}
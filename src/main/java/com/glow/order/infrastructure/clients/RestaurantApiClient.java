package com.glow.order.infrastructure.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.UUID;

@RegisterRestClient(configKey = "restaurant-api")
@Path("/restaurants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RestaurantApiClient {

    @POST
    @Path("/{restaurantId}/orders")
    void notifyRestaurant(@PathParam("restaurantId") UUID restaurantId,
                          RestaurantNotificationRequest request);
}
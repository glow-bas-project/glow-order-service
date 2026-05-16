package com.glow.order.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class RestaurantAddressEntity {

    @Column(name = "restaurant_address")
    private String address;

    @Column(name = "restaurant_city")
    private String city;

    @Column(name = "restaurant_country")
    private String country;

    @Column(name = "restaurant_longitude")
    private Double longitude;

    @Column(name = "restaurant_latitude")
    private Double latitude;

    public RestaurantAddressEntity() {
    }

    public RestaurantAddressEntity(String address, String city, String country, Double longitude, Double latitude) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
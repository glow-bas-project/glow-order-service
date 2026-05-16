package com.glow.order.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DeliveryAddressEntity {

    @Column(name = "delivery_address")
    private String address;

    @Column(name = "delivery_city")
    private String city;

    @Column(name = "delivery_country")
    private String country;

    @Column(name = "delivery_longitude")
    private Double longitude;

    @Column(name = "delivery_latitude")
    private Double latitude;

    public DeliveryAddressEntity() {
    }

    public DeliveryAddressEntity(String address, String city, String country, Double longitude, Double latitude) {
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
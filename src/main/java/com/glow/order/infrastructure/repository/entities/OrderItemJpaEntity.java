package com.glow.order.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemJpaEntity {
    @Column(name = "menu_item_id")
    private String menuItemId;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_quantity")
    private Integer quantity;

    @Column(name = "item_price")
    private Integer price;

    public OrderItemJpaEntity() {
    }

    public OrderItemJpaEntity(String menuItemId, String name, Integer quantity, Integer price) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

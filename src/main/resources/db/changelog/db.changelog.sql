--liquibase formatted sql
--changeset sarh:08_05_2026-1
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    transfer_group VARCHAR(255) NOT NULL,
    stripe_payment_intent_id VARCHAR(255) NOT NULL,
    platform_fee_amount INTEGER NOT NULL,
    total_price INTEGER NOT NULL,
    courier_transfer_id VARCHAR(255),
    restaurant_transfer_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    delivery_city VARCHAR(255) NOT NULL,
    delivery_country VARCHAR(255) NOT NULL,
    delivery_longitude DOUBLE PRECISION NOT NULL,
    delivery_latitude DOUBLE PRECISION NOT NULL,
    restaurant_address VARCHAR(255) NOT NULL,
    restaurant_city VARCHAR(255) NOT NULL,
    restaurant_country VARCHAR(255) NOT NULL,
    restaurant_longitude DOUBLE PRECISION NOT NULL,
    restaurant_latitude DOUBLE PRECISION NOT NULL
);

--changeset sarh:10_06_2026-2
CREATE TABLE IF NOT EXISTS order_items (
    order_id VARCHAR(255) NOT NULL,
    menu_item_id VARCHAR(255) NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    item_quantity INTEGER NOT NULL,
    item_price INTEGER NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
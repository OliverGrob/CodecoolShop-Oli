package com.codecool.shop.model;

public enum ShoppingCartStatuses {
    IN_CART ("in_cart"),
    CHECKED ("checked"),
    PAID ("paid"),
    CONFIRMED ("confirmed"),
    SHIPPED ("shipped");

    private final String cartStatus;


    ShoppingCartStatuses(String cartStatus) {
        this.cartStatus = cartStatus;
    }


    public String getCartStatus() {
        return this.cartStatus;
    }

}

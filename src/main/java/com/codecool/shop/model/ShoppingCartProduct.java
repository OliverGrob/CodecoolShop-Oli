package com.codecool.shop.model;

public class ShoppingCartProduct {

    private ShoppingCart shoppingCart;
    private Product product;
    private int amount;


    public ShoppingCartProduct(ShoppingCart shoppingCart, Product product, int amount) {
        this.shoppingCart = shoppingCart;
        this.product = product;
        this.amount = amount;
    }


    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}

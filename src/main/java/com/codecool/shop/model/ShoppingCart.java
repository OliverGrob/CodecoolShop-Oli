package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart {

    private int id;
    private User user;
    private Date time;
    private ShoppingCartStatus status;

    private float totalPrice = 0;
    private List<Product> productsInCart = new ArrayList<>();


    public ShoppingCart(int id, User user, Date time, ShoppingCartStatus status) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ShoppingCartStatus getStatus() {
        return status;
    }

    public void setStatus(ShoppingCartStatus status) {
        this.status = status;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Product> getProductsInCart() {
        return productsInCart;
    }

    public void setProductsInCart(List<Product> productsInCart) {
        this.productsInCart = productsInCart;
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "user: %2$s, " +
                        "time: %3$s, " +
                        "status: %4$s, " +
                        "totalPrice: %5$s, ",
                this.id,
                this.user,
                this.time.toString(),
                this.status,
                this.totalPrice);
    }

}

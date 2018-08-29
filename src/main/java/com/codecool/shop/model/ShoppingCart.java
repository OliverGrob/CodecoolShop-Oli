package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart {

    private int id;
    private int userId;
    private Date time;
    private ShoppingCartStatus status;

    private float totalPrice = 0;
    private List<Product> productsInCart = new ArrayList<>();


    public ShoppingCart(int id, int userId, Date time, ShoppingCartStatus status) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
                        "userId: %2$s, " +
                        "time: %3$s, " +
                        "status: %4$s, " +
                        "totalPrice: %5$s, ",
                this.id,
                this.userId,
                this.time.toString(),
                this.status,
                this.totalPrice);
    }

}

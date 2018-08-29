package com.codecool.shop.dao;

import com.codecool.shop.model.*;

import java.util.Date;
import java.util.List;

public interface ShoppingCartDao {

    void add(int userId, Date time, ShoppingCartStatus status);

    ShoppingCart find(int id);
    ShoppingCart findActiveCart();

    void remove(int id);

    List<ShoppingCart> getAll();

    float calculateTotalPrice(List<Product> allProductsInCart);

}

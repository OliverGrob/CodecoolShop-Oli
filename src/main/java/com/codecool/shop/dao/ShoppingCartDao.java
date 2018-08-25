package com.codecool.shop.dao;

import com.codecool.shop.model.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ShoppingCartDao {

    void add(int userId, Date time, ShoppingCartStatuses status);
    void addProductToShoppingCart(Product product);

    ShoppingCart find(int id);
    ShoppingCart findActiveCart();

    void remove(int id);
    void removeProductFromShoppingCart(Product product);

    List<ShoppingCart> getAll();

    Set<Product> getProductNumberInActiveCart();
    List<Product> getAllProductsInActiveCart();
    Integer getProductQuantityByProductIdInActiveCart(int id);

}

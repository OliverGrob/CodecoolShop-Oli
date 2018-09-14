package com.codecool.shop.dao;

import com.codecool.shop.model.*;

import java.util.Date;
import java.util.List;

public interface ShoppingCartDao {

    void add(int userId, Date time);

    ShoppingCart find(int id);
    ShoppingCart findActiveCartForUser(int userId);
    void changeCartStatus(int userId, int shoppingCartId, ShoppingCartStatus statusTo);

    void remove(int id);

    List<ShoppingCart> getAll();

}

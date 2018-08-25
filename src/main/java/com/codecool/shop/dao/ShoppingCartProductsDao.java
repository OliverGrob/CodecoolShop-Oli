package com.codecool.shop.dao;

import com.codecool.shop.model.ShoppingCartProduct;

import java.util.List;

public interface ShoppingCartProductsDao {

    void addProductToShoppingCart(int shoppingCartId, int productId);

    void removeProductFromShoppingCart(int shoppingCartId, int productId);

    List<ShoppingCartProduct> getAll();

}

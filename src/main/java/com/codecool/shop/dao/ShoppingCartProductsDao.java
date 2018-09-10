package com.codecool.shop.dao;

import com.codecool.shop.model.ShoppingCartProduct;

import java.util.List;

public interface ShoppingCartProductsDao {

    int addProductToShoppingCart(int shoppingCartId, int productId);

    int removeProductFromShoppingCart(int shoppingCartId, int productId);

    List<ShoppingCartProduct> getShoppingCartProductsByUser(int userId);

    int calculateTotalItemNumber(List<ShoppingCartProduct> shoppingCartProducts);
    float calculateTotalPrice(List<ShoppingCartProduct> shoppingCartProducts);

}

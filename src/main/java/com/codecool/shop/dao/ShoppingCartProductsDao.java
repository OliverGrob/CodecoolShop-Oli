package com.codecool.shop.dao;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCartProduct;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ShoppingCartProductsDao {

    void addProductToShoppingCart(int shoppingCartId, int productId);

    void removeProductFromShoppingCart(int shoppingCartId, int productId);

    List<ShoppingCartProduct> getShoppingCartProductsByShoppingCartId(int shoppingCartId);
    List<ShoppingCartProduct> getAll();
    Integer getProductQuantityByProductIdInActiveCart(int shoppingCartId, int productId);
    Map<Integer, Integer> getAllProductQuantity(int shoppingCartId, Set<Product> products);

}

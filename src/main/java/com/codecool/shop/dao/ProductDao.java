package com.codecool.shop.dao;

import com.codecool.shop.model.ShoppingCartProduct;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.util.List;
import java.util.Set;

public interface ProductDao {

    void add(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier);
    Product find(int id);
    Product find(String name);

    void remove(int id);

    List<Product> getAll();
    List<Product> getBySupplier(int supplierId);
    List<Product> getByProductCategory(int productCategoryId);
    List<Product> getAllProductsForShoppingCart(List<ShoppingCartProduct> shoppingCartProducts);
    Set<Product> getProductsForShoppingCart(List<ShoppingCartProduct> shoppingCartProducts);

}

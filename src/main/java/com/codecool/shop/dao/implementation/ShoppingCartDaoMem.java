package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.ShoppingCartStatuses;

import java.util.*;

public class ShoppingCartDaoMem implements ShoppingCartDao {

    private List<ShoppingCart> data = new ArrayList<>();
    private static ShoppingCartDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ShoppingCartDaoMem() {
    }

    public static ShoppingCartDaoMem getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoMem();
        }
        return instance;
    }

    @Override
    public void add(int userId, Date time, ShoppingCartStatuses status) {
        data.add(new ShoppingCart(data.size() + 1, userId, time, status));
    }

    @Override
    public void addProductToShoppingCart(Product product) {
        ShoppingCart shoppingCart = this.findActiveCart();
        shoppingCart.getProductsInCart().add(product);
        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + product.getDefaultPrice());
    }

    @Override
    public ShoppingCart find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public ShoppingCart findActiveCart() {
        return data.stream().filter(t -> t.getStatus().equals(ShoppingCartStatuses.IN_CART)).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public void removeProductFromShoppingCart(Product product) {
        this.findActiveCart().getProductsInCart().remove(product);
    }

    @Override
    public List<ShoppingCart> getAll() {
        return data;
    }

    @Override
    public LinkedHashSet<Product> getProductNumberInActiveCart() {
        return new LinkedHashSet<>(this.findActiveCart().getProductsInCart());
    }

    @Override
    public List<Product> getAllProductsInActiveCart() {
        return this.findActiveCart().getProductsInCart();
    }

    @Override
    public Integer getProductQuantityByProductIdInActiveCart(int id) {
        return (int) this.findActiveCart().getProductsInCart().stream().filter(t -> t.getId() == id).count();
    }
}

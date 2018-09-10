package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.ShoppingCartStatus;

import java.util.*;
import java.util.stream.Collectors;

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
    public void add(int userId, Date time) {
//        data.add(new ShoppingCart(data.size() + 1, userId, time, ShoppingCartStatus.IN_CART));
    }

//    @Override
//    public void addProductToShoppingCart(int shoppingCartId, int productId) {
//        ShoppingCart shoppingCart = this.findActiveCart();
//        Product product = this.getProductNumberInActiveCart().stream().filter(t -> t.getId() == productId).collect(Collectors.toList()).get(0);
//        shoppingCart.getProductsInCart().add(product);
//        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + product.getDefaultPrice());
//    }

    @Override
    public ShoppingCart find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public ShoppingCart findActiveCartForUser(int userId) {
        return data.stream().filter(t -> t.getStatus().equals(ShoppingCartStatus.IN_CART)).findFirst().orElse(null);
    }

    @Override
    public void changeCartStatus(int userId, ShoppingCartStatus statusFrom, ShoppingCartStatus statusTo) {

    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

//    @Override
//    public void removeProductFromShoppingCart(Product product) {
//        this.findActiveCart().getProductsInCart().remove(product);
//    }

    @Override
    public List<ShoppingCart> getAll() {
        return data;
    }

//    @Override
//    public LinkedHashSet<Product> getProductNumberInActiveCart() {
//        return new LinkedHashSet<>(this.findActiveCart().getProductsInCart());
//    }
//
//    @Override
//    public List<Product> getAllProductsInActiveCart() {
//        return this.findActiveCart().getProductsInCart();
//    }
//
//    @Override
//    public Integer getProductQuantityByProductIdInActiveCart(int id) {
//        return (int) this.findActiveCart().getProductsInCart().stream().filter(t -> t.getId() == id).count();
//    }
}

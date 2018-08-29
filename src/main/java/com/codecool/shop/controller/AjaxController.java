package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCartProduct;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/change-quantity"})
public class AjaxController extends HttpServlet {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> newData = new HashMap<>();

        int activeCartId = shoppingCartDataStore.findActiveCart().getId();
        List<ShoppingCartProduct> shoppingCartProductsInCart = shoppingCartProductsDataStore.getShoppingCartProductsByShoppingCartId(activeCartId);
        List<Product> productsInCart = productDataStore.getAllProductsForShoppingCart(shoppingCartProductsInCart);

        int totalItemNumInCart = productsInCart.size();

        newData.put("totalItemsInCart", totalItemNumInCart);
        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> newData = new HashMap<>();

        int productId = Integer.parseInt(req.getParameter("id"));
        int shoppingCartId = shoppingCartDataStore.findActiveCart().getId();

        if (req.getParameter("quantity").equals("decrease")) {
            shoppingCartProductsDataStore.removeProductFromShoppingCart(shoppingCartDataStore.findActiveCart().getId(), productId);
        } else if (req.getParameter("quantity").equals("increase")) {
            shoppingCartProductsDataStore.addProductToShoppingCart(shoppingCartDataStore.findActiveCart().getId(), productId);
        }

        int activeCartId = shoppingCartDataStore.findActiveCart().getId();
        List<ShoppingCartProduct> shoppingCartProductsInCart = shoppingCartProductsDataStore.getShoppingCartProductsByShoppingCartId(activeCartId);
        List<Product> productsInCart = productDataStore.getAllProductsForShoppingCart(shoppingCartProductsInCart);

        int newQuantity = shoppingCartProductsDataStore.getProductQuantityByProductIdInActiveCart(shoppingCartId, productId);
        int newTotalItems = productsInCart.size();
        int newTotalPrice = Math.round(shoppingCartDataStore.calculateTotalPrice(productsInCart)* 100) / 100;

        newData.put("productId", productId);
        newData.put("newQuantity", newQuantity);
        newData.put("newTotalItems", newTotalItems);
        newData.put("newTotalPrice", newTotalPrice);
        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

}

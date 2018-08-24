package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.ShoppingCartDaoMem;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/change-quantity"})
public class AjaxController extends HttpServlet {
    private ProductDao productDataStore = ProductDaoMem.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoMem.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> newData = new HashMap<>();

        newData.put("totalItemsInCart", shoppingCartDataStore.getAllProductsInActiveCart().size());
        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> newData = new HashMap<>();

        int productId = Integer.parseInt(req.getParameter("id"));

        if (req.getParameter("quantity").equals("decrease")) {
            shoppingCartDataStore.removeProductFromShoppingCart(productDataStore.find(productId));
        } else if (req.getParameter("quantity").equals("increase")) {
            shoppingCartDataStore.addProductToShoppingCart(productDataStore.find(productId));
        }

        int newQuantity = (shoppingCartDataStore.getProductQuantityByProductIdInActiveCart(productId) != null) ?
                shoppingCartDataStore.getProductQuantityByProductIdInActiveCart(productId) : 0;
        int newTotalItems = shoppingCartDataStore.findActiveCart().getProductsInCart().size();
        int newTotalPrice = Math.round(shoppingCartDataStore.findActiveCart().getTotalPrice() * 100) / 100;

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

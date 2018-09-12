package com.codecool.shop.controller;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.ShoppingCartProduct;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/change-quantity"})
public class ShoppingCartAjaxController extends HttpServlet {
    private SessionManager sessionManager = SessionManager.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        Map<String, Integer> newData = new HashMap<>();

        newData.put("totalItemsInCart",
                shoppingCartProductsDataStore.calculateTotalItemNumber(
                        shoppingCartProductsDataStore.getShoppingCartProductsByUser((Integer) session.getAttribute("userId"))
                ));
        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        Map<String, Integer> newData = new HashMap<>();

        int userId = (Integer) session.getAttribute("userId");
        int newQuantity = 0;
        int productId = Integer.parseInt(req.getParameter("id"));
        int shoppingCartId = shoppingCartDataStore.findActiveCartForUser(userId).getId();
        List<ShoppingCartProduct> shoppingCartProducts = shoppingCartProductsDataStore.getShoppingCartProductsByUser(userId);

        if (req.getParameter("quantity").equals("decrease")) {
            newQuantity = shoppingCartProductsDataStore.removeProductFromShoppingCart(shoppingCartId, productId);
        } else if (req.getParameter("quantity").equals("increase")) {
            newQuantity = shoppingCartProductsDataStore.addProductToShoppingCart(shoppingCartId, productId);
        }

        int newTotalItems = shoppingCartProductsDataStore.calculateTotalItemNumber(shoppingCartProducts);
        int newTotalPrice = Math.round(shoppingCartProductsDataStore.calculateTotalPrice(shoppingCartProducts) * 100) / 100;

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

package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.ShoppingCartProduct;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCartController extends HttpServlet {
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = req.getSession(false);

        if (session == null) {
            session = req.getSession(true);
            session.setAttribute("userId", null);
            resp.sendRedirect("/");
        } else {
            if (session.getAttribute("userId") == null) {
                resp.sendRedirect("/");
            } else {
                int userId = (Integer) session.getAttribute("userId");
                List<ShoppingCartProduct> shoppingCartProducts = shoppingCartProductsDataStore.getShoppingCartProductsByUser(userId);

                int totalItemNumInCart = shoppingCartProductsDataStore.calculateTotalItemNumber(shoppingCartProducts);
                float totalPrice = shoppingCartProductsDataStore.calculateTotalPrice(shoppingCartProducts);

                context.setVariable("userId", userId);
                context.setVariable("products", shoppingCartProducts);
                context.setVariable("totalItemNum", totalItemNumInCart);
                context.setVariable("totalPrice", totalPrice);
                context.setVariable("category", productCategoryDataStore.getAll());
                context.setVariable("supplier", supplierDataStore.getAll());

                engine.process("cart/shopping_cart.html", context, resp.getWriter());
            }
        }
    }

}


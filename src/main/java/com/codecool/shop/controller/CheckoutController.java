package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.ShoppingCartProduct;
import com.codecool.shop.model.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
    private SessionManager sessionManager = SessionManager.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();
    private UserDao userHanlder = UserDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        User user = userHanlder.find((Integer) session.getAttribute("userId"));
        List<ShoppingCartProduct> shoppingCartProducts = shoppingCartProductsDataStore.getShoppingCartProductsByUser(user.getId());

        int totalItemNumInCart = shoppingCartProductsDataStore.calculateTotalItemNumber(shoppingCartProducts);
        float totalPrice = shoppingCartProductsDataStore.calculateTotalPrice(shoppingCartProducts);

        context.setVariable("user", user);
        context.setVariable("userId", user.getId());
        context.setVariable("products", shoppingCartProducts);
        context.setVariable("totalItemNum", totalItemNumInCart);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("supplier", supplierDataStore.getAll());

        engine.process("cart/checkout.html", context, resp.getWriter());
    }

}

package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
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
import java.util.Set;

@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCartController extends HttpServlet {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
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
                // Getting Products for shopping cart: (1) Getting active Cart's id -> (2) Getting ShoppingCartProducts -> (3) Getting Products
                // 1
                int activeCartId = shoppingCartDataStore.findActiveCart().getId();
                // 2
                List<ShoppingCartProduct> shoppingCartProductsInCart = shoppingCartProductsDataStore.getShoppingCartProductsByShoppingCartId(activeCartId);
                // 3
                Set<Product> productsInCart = productDataStore.getProductsForShoppingCart(shoppingCartProductsInCart);
                List<Product> allProductsInCart = productDataStore.getAllProductsForShoppingCart(shoppingCartProductsInCart);

                // Getting total item count
                int totalItemNumInCart = allProductsInCart.size();
                // Getting total price for cart
                float totalPrice = shoppingCartDataStore.calculateTotalPrice(allProductsInCart);

                context.setVariable("products", productsInCart);
                context.setVariable("shopping_cart_data", shoppingCartProductsDataStore.getAllProductQuantity(activeCartId, productsInCart));
                context.setVariable("totalItemNum", totalItemNumInCart);
                context.setVariable("totalPrice", totalPrice);
                context.setVariable("category", productCategoryDataStore.getAll());
                context.setVariable("supplier", supplierDataStore.getAll());

                engine.process("cart/shopping_cart.html", context, resp.getWriter());
            }
        }
    }

}


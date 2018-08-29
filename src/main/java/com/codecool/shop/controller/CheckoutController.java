package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCartProduct;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
    private ProductDao productDataStore = ProductDaoJDBC.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

//        HttpSession session = req.getSession();
//        context.setVariable("shopping_cart", session.getAttribute("shopping_cart"));
//        context.setVariable("totalItems", session.getAttribute("totalItems"));
//        context.setVariable("totalPrice", session.getAttribute("totalPrice"));

        int activeCartId = shoppingCartDataStore.findActiveCart().getId();
        List<ShoppingCartProduct> shoppingCartProductsInCart = shoppingCartProductsDataStore.getShoppingCartProductsByShoppingCartId(activeCartId);
        Set<Product> productsInCart = productDataStore.getProductsForShoppingCart(shoppingCartProductsInCart);
        List<Product> allProductsInCart = productDataStore.getAllProductsForShoppingCart(shoppingCartProductsInCart);

        int totalItemNumInCart = allProductsInCart.size();
        float totalPrice = shoppingCartDataStore.calculateTotalPrice(allProductsInCart);

        context.setVariable("products", productsInCart);
        context.setVariable("shopping_cart_data", shoppingCartProductsDataStore.getAllProductQuantity(activeCartId, productsInCart));
        context.setVariable("totalItemNum", totalItemNumInCart);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("supplier", supplierDataStore.getAll());

        engine.process("cart/checkout.html", context, resp.getWriter());
    }
}

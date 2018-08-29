package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
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
        } else {
            context.setVariable("userId", session.getAttribute("userId"));
        }

        String categoryNameFromUrl = req.getParameter("category");
        String supplierNameFromUrl = req.getParameter("supplier");

        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("supplier", supplierDataStore.getAll());
        context.setVariable("category_name", getCategoryImg(categoryNameFromUrl));
        context.setVariable("products", getProducts(categoryNameFromUrl, supplierNameFromUrl));

        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = req.getSession(false);

        if (session == null) {
            session = req.getSession(true);
            session.setAttribute("userId", null);
        } else {
            context.setVariable("userId", session.getAttribute("userId"));
        }

        String categoryNameFromUrl = req.getParameter("category");
        String supplierNameFromUrl = req.getParameter("supplier");

        shoppingCartProductsDataStore.addProductToShoppingCart(shoppingCartDataStore.findActiveCart().getId(),
                                                               Integer.parseInt(req.getParameter("product")));

        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("supplier", supplierDataStore.getAll());
        context.setVariable("category_name", getCategoryImg(categoryNameFromUrl));
        context.setVariable("products", getProducts(categoryNameFromUrl, supplierNameFromUrl));

        engine.process("product/index.html", context, resp.getWriter());

    }

    private List<Product> getProducts(String categoryNameFromUrl, String supplierNameFromUrl) {
        if (productCategoryDataStore.find(categoryNameFromUrl) != null) {
            return productDataStore.getByProductCategory(productCategoryDataStore.find(categoryNameFromUrl).getId());
        } else if (supplierDataStore.find(supplierNameFromUrl) != null) {
            return productDataStore.getBySupplier(supplierDataStore.find(supplierNameFromUrl).getId());
        }

        return productDataStore.getAll();
    }

    private String getCategoryImg (String categoryNameFromUrl) {
        if (categoryNameFromUrl != null) {
            return categoryNameFromUrl;
        } else {
            return "Cloud";
        }
    }

}

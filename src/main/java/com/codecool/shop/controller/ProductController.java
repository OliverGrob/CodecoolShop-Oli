package com.codecool.shop.controller;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private SessionManager sessionManager = SessionManager.getInstance();
    private ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJDBC.getInstance();
    private SupplierDao supplierDataStore = SupplierDaoJDBC.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
    private ShoppingCartProductsDao shoppingCartProductsDataStore = ShoppingCartProductsDaoJDBC.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = sessionManager.getHttpSession(req);

        if (session != null) context.setVariable("userId", session.getAttribute("userId"));

        String categoryNameFromUrl = req.getParameter("category");
        String supplierNameFromUrl = req.getParameter("supplier");

        List<ProductCategory> productCategories = productCategoryDataStore.getAll();
        List<Supplier> suppliers = supplierDataStore.getAll();

        context.setVariable("category", productCategories);
        context.setVariable("supplier", suppliers);
        context.setVariable("category_name", getCategoryImg(categoryNameFromUrl));
        context.setVariable("products", getProducts(productCategories, categoryNameFromUrl,
                                                        suppliers, supplierNameFromUrl));

        engine.process("product/index.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        String categoryNameFromUrl = req.getParameter("category");
        String supplierNameFromUrl = req.getParameter("supplier");

        int shoppingCartId = shoppingCartDataStore.findActiveCartForUser((Integer) session.getAttribute("userId")).getId();
        shoppingCartProductsDataStore.addProductToShoppingCart(shoppingCartId, Integer.parseInt(req.getParameter("product")));

        List<ProductCategory> productCategories = productCategoryDataStore.getAll();
        List<Supplier> suppliers = supplierDataStore.getAll();

        context.setVariable("category", productCategories);
        context.setVariable("supplier", suppliers);
        context.setVariable("category_name", getCategoryImg(categoryNameFromUrl));
        context.setVariable("products", getProducts(productCategories, categoryNameFromUrl,
                                                        suppliers, supplierNameFromUrl));

        engine.process("product/index.html", context, resp.getWriter());

    }

    private List<Product> getProducts(List<ProductCategory> productCategories, String categoryNameFromUrl,
                                      List<Supplier> suppliers, String supplierNameFromUrl) {

        List<ProductCategory> prodCats = productCategories.stream()
                .filter(cat -> cat.getName().equals(categoryNameFromUrl))
                .collect(Collectors.toList());

        List<Supplier> supps = suppliers.stream()
                .filter(supp -> supp.getName().equals(supplierNameFromUrl))
                .collect(Collectors.toList());

        if (prodCats.size() != 0) {
            return prodCats.get(0).getProducts();
        } else if (supps.size() != 0) {
            return supps.get(0).getProducts();
        }

        List<Product> allProducts = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            allProducts.addAll(productCategory.getProducts());
        }

        return allProducts;
    }

    private String getCategoryImg (String categoryNameFromUrl) {
        if (categoryNameFromUrl != null) {
            return categoryNameFromUrl;
        } else {
            return "Cloud";
        }
    }

}

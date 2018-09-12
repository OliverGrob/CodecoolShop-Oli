package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.*;

public class ProductCategoryDaoJDBC implements ProductCategoryDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ProductCategoryDaoJDBC instance = null;


    public static ProductCategoryDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC();
        }
        return instance;
    }

    private List<ProductCategory> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<ProductCategory> productCategories = new ArrayList<>();
        List<String> productCategoryNames = new ArrayList<>();
        ProductCategory prodCat = null;

        for (Map singleRow : resultRowsFromQuery) {
            if (!productCategoryNames.contains((String) singleRow.get("name"))) {
                productCategoryNames.add((String) singleRow.get("name"));
                prodCat = new ProductCategory((Integer) singleRow.get("id"),
                        (String) singleRow.get("name"),
                        (String) singleRow.get("description"),
                        (String) singleRow.get("department"));
                productCategories.add(prodCat);
            }
            productCategories.get(productCategories.indexOf(prodCat)).addProduct(
                    new Product((Integer) singleRow.get("prod_id"),
                        (String) singleRow.get("prod_name"),
                        (float) ((double) singleRow.get("default_price")),
                        (String) singleRow.get("currency_string"),
                        (String) singleRow.get("prod_desc"),
                        new ProductCategory((Integer) singleRow.get("id"),
                                (String) singleRow.get("name"),
                                (String) singleRow.get("department"),
                                (String) singleRow.get("description")),
                        new Supplier((Integer) singleRow.get("supp_id"),
                                (String) singleRow.get("supp_name"),
                                (String) singleRow.get("supp_desc"))));
        }

        return productCategories;
    }

    @Override
    public void add(String name, String description, String department) {
        controller.executeQuery(
        "INSERT INTO product_category (id, name, description, department) " +
                "VALUES (DEFAULT, ?, ?, ?);",
            Arrays.asList(name, description, department));
    }

    @Override
    public ProductCategory find(int id) {
        List<Map<String, Object>> productCategories = controller.executeQueryWithReturnValue(
        "SELECT product_category.id, " +
                  "product_category.name, " +
                  "product_category.description, " +
                  "product_category.department, " +
                  "product.id AS prod_id," +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product_category " +
                  "JOIN product ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                "WHERE product_category.id = ? " +
                "ORDER BY product_category.id;",
            Collections.singletonList(id));

        return (productCategories.size() != 0) ? this.objectCreator(productCategories).get(0) : null;
    }

    @Override
    public ProductCategory find(String name) {
        List<Map<String, Object>> productCategories = controller.executeQueryWithReturnValue(
        "SELECT product_category.id, " +
                  "product_category.name, " +
                  "product_category.description, " +
                  "product_category.department, " +
                  "product.id AS prod_id," +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product_category " +
                  "JOIN product ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                "WHERE product_category.name LIKE ? " +
                "ORDER BY product_category.id;",
            Collections.singletonList(name));

        return (productCategories.size() != 0) ? this.objectCreator(productCategories).get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM product_category WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<ProductCategory> getAll() {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT product_category.id, " +
                  "product_category.name, " +
                  "product_category.description, " +
                  "product_category.department, " +
                  "product.id AS prod_id, " +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product_category " +
                  "JOIN product ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                "ORDER BY product_category.id;",
            Collections.emptyList()));
    }

}
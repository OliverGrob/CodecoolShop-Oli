package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.*;

public class ProductDaoJDBC implements ProductDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ProductDaoJDBC instance = null;


    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    private List<Product> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<Product> products = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            products.add(new Product((Integer) singleRow.get("id"),
                    (String) singleRow.get("name"),
                    (float) ((double) singleRow.get("default_price")),
                    (String) singleRow.get("currency_string"),
                    (String) singleRow.get("description"),
                    new ProductCategory((Integer) singleRow.get("prod_cat_id"),
                            (String) singleRow.get("prod_cat_name"),
                            (String) singleRow.get("prod_cat_dep"),
                            (String) singleRow.get("prod_cat_desc")),
                    new Supplier((Integer) singleRow.get("supp_id"),
                            (String) singleRow.get("supp_name"),
                            (String) singleRow.get("supp_desc"))));
        }

        return products;
    }

    @Override
    public void add(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier) {
        controller.executeQuery(
        "INSERT INTO product (id, name, description, default_price, currency_string, supplier_id, product_category_id) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);",
            Arrays.asList(name, description, defaultPrice, currencyString, supplier.getId(), productCategory.getId())
        );
    }

    @Override
    public Product find(int id) {
        List<Map<String, Object>> products = controller.executeQueryWithReturnValue(
        "SELECT product.id, product.name, product.default_price, product.currency_string, product.description, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.description AS prod_cat_desc, " +
                  "product_category.department AS prod_cat_dep, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                "WHERE product.id = ?;",
            Collections.singletonList(id));

        return (products.size() != 0) ? this.objectCreator(products).get(0) : null;
    }

    @Override
    public Product find(String name) {
        List<Map<String, Object>> products = controller.executeQueryWithReturnValue(
        "SELECT product.id, product.name, product.default_price, product.currency_string, product.description, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.description AS prod_cat_desc, " +
                  "product_category.department AS prod_cat_dep, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                "WHERE product.name LIKE ?;",
            Collections.singletonList(name));

        return (products.size() != 0) ? this.objectCreator(products).get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM product WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<Product> getAll() {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT product.id, product.name, product.default_price, product.currency_string, product.description, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.description AS prod_cat_desc, " +
                  "product_category.department AS prod_cat_dep, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc " +
                "FROM product " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id;",
            Collections.emptyList()));
    }

}
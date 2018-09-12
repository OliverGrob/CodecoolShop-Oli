package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.util.*;

public class SupplierDaoJDBC implements SupplierDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static SupplierDaoJDBC instance = null;


    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    private List<Supplier> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<Supplier> suppliers = new ArrayList<>();
        List<String> supplierNames= new ArrayList<>();
        Supplier prodCat = null;

        for (Map singleRow : resultRowsFromQuery) {
            if (!supplierNames.contains((String) singleRow.get("name"))) {
                supplierNames.add((String) singleRow.get("name"));
                prodCat = new Supplier((Integer) singleRow.get("id"),
                        (String) singleRow.get("name"),
                        (String) singleRow.get("description"));
                suppliers.add(prodCat);
            }
            suppliers.get(suppliers.indexOf(prodCat)).addProduct(
                    new Product((Integer) singleRow.get("prod_id"),
                            (String) singleRow.get("prod_name"),
                            (float) ((double) singleRow.get("default_price")),
                            (String) singleRow.get("currency_string"),
                            (String) singleRow.get("prod_desc"),
                            new ProductCategory((Integer) singleRow.get("prod_cat_id"),
                                    (String) singleRow.get("prod_cat_name"),
                                    (String) singleRow.get("prod_cat_dep"),
                                    (String) singleRow.get("prod_cat_desc")),
                            new Supplier((Integer) singleRow.get("id"),
                                    (String) singleRow.get("name"),
                                    (String) singleRow.get("description"))));
        }

        return suppliers;
    }

    @Override
    public void add(String name, String description) {
        controller.executeQuery(
        "INSERT INTO supplier(id, name, description) " +
                "VALUES (DEFAULT, ?, ?);",
            Arrays.asList(name, description));
    }

    @Override
    public Supplier find(int id) {
        List<Map<String, Object>> suppliers = controller.executeQueryWithReturnValue(
        "SELECT supplier.id, " +
                  "supplier.name, " +
                  "supplier.description, " +
                  "product.id AS prod_id, " +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.department AS prod_cat_dep, " +
                  "product_category.description AS prod_cat_desc " +
                "FROM supplier " +
                  "JOIN product ON product.product_category_id = supplier.id " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                "WHERE supplier.id = ? " +
                "ORDER BY supplier.id;",
            Collections.singletonList(id));

        return (suppliers.size() != 0) ? this.objectCreator(suppliers).get(0) : null;
    }

    @Override
    public Supplier find(String name) {
        List<Map<String, Object>> suppliers = controller.executeQueryWithReturnValue(
        "SELECT supplier.id, " +
                  "supplier.name, " +
                  "supplier.description, " +
                  "product.id AS prod_id, " +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.department AS prod_cat_dep, " +
                  "product_category.description AS prod_cat_desc " +
                "FROM supplier " +
                  "JOIN product ON product.product_category_id = supplier.id " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                "WHERE supplier.name LIKE ? " +
                "ORDER BY supplier.id;",
            Collections.singletonList(name));

        return (suppliers.size() != 0) ? this.objectCreator(suppliers).get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM supplier WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<Supplier> getAll() {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT supplier.id, " +
                  "supplier.name, " +
                  "supplier.description, " +
                  "product.id AS prod_id, " +
                  "product.name AS prod_name, " +
                  "product.default_price, " +
                  "product.currency_string, " +
                  "product.description AS prod_desc, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.department AS prod_cat_dep, " +
                  "product_category.description AS prod_cat_desc " +
                "FROM supplier " +
                  "JOIN product ON product.product_category_id = supplier.id " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                "ORDER BY supplier.id;",
            Collections.emptyList()));
    }

}
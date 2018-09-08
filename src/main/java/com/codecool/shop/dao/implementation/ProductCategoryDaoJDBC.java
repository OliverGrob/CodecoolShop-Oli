package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.ProductCategory;

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

    private ProductCategory singleObjectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        Map<String, Object> singleRow = resultRowsFromQuery.get(0);

        return new ProductCategory((Integer) singleRow.get("id"),
                (String) singleRow.get("name"),
                (String) singleRow.get("description"),
                (String) singleRow.get("department"));
    }

    private List<ProductCategory> multipleObjectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<ProductCategory> productCategories = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            productCategories.add(new ProductCategory((Integer) singleRow.get("id"),
                    (String) singleRow.get("name"),
                    (String) singleRow.get("description"),
                    (String) singleRow.get("department")));
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
        "SELECT * FROM product_category WHERE id = ?;",
            Collections.singletonList(id));

        return (productCategories.size() != 0) ? this.singleObjectCreator(productCategories) : null;
    }

    @Override
    public ProductCategory find(String name) {
        List<Map<String, Object>> productCategories = controller.executeQueryWithReturnValue(
        "SELECT * FROM product_category WHERE name LIKE ?;",
            Collections.singletonList(name));

        return (productCategories.size() != 0) ? this.singleObjectCreator(productCategories) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM product_category WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<ProductCategory> getAll() {
        return this.multipleObjectCreator(controller.executeQueryWithReturnValue(
        "SELECT * FROM product_category;",
            Collections.emptyList()));
    }

}
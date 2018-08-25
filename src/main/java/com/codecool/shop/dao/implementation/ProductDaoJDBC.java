package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ProductDaoJDBC instance = null;


    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    private List<Product> executeQueryWithReturnValue(String query) {
        List<Product> resultList = new ArrayList<>();

        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Product data = new Product(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("default_price"),
                        resultSet.getString("currency_string"),
                        resultSet.getString("description"),
                        ProductCategoryDaoJDBC.getInstance().find(resultSet.getInt("product_category_id")),
                        SupplierDaoJDBC.getInstance().find(resultSet.getInt("supplier_id")));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void add(String name, float defaultPrice, String currencyString, String description, int productCategoryId, int supplierId) {
        controller.executeQuery(
            "INSERT INTO product (id, name, description, default_price, currency_string, supplier_id, product_category_id)" +
                "VALUES (DEFAULT, '" + name + "', '" + description + "', " + defaultPrice + ", " +
                         currencyString + ", " + supplierId + ", " + productCategoryId + ";"
        );
    }

    @Override
    public Product find(int id) {
        return executeQueryWithReturnValue(
            "SELECT * FROM product WHERE id = '" + id + "';"
        ).get(0);
    }

    @Override
    public Product find(String name) {
        return executeQueryWithReturnValue(
            "SELECT * FROM product WHERE name LIKE '" + name + "';"
        ).get(0);
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
            "DELETE FROM product WHERE id = '" + id + "';"
        );
    }

    @Override
    public List<Product> getAll() {
        return executeQueryWithReturnValue(
            "SELECT * FROM product;"
        );
    }

    @Override
    public List<Product> getBy(int supplierId) {
        return executeQueryWithReturnValue(
            "SELECT * FROM product WHERE supplier_id = '" + supplierId + "';"
        );
    }

    @Override
    public List<Product> getBy(int productCategoryId) {
        return executeQueryWithReturnValue(
            "SELECT * FROM product WHERE product_category_id = '" + productCategoryId + "';"
        );
    }

}
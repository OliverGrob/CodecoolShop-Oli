package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShoppingCartDaoJDBC implements ShoppingCartDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartDaoJDBC instance = null;


    public static ShoppingCartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoJDBC();
        }
        return instance;
    }

    public List<ShoppingCart> executeQueryWithReturnValue(String query) {
        List<ShoppingCart> resultList = new ArrayList<>();

        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                ShoppingCart data = new ShoppingCart(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDate("time"),
                        resultSet.getString("status"));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void add(ShoppingCart shoppingCart) {

    }

    @Override
    public void addProductToShoppingCart(Product product) {

    }

    @Override
    public ShoppingCart find(int id) {
        return null;
    }

    @Override
    public ShoppingCart findActiveCart() {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void removeProductFromShoppingCart(Product product) {

    }

    @Override
    public List<ShoppingCart> getAll() {
        return null;
    }

    @Override
    public Set<Product> getProductNumberInActiveCart() {
        return null;
    }

    @Override
    public List<Product> getAllProductsInActiveCart() {
        return null;
    }

    @Override
    public Integer getProductQuantityByProductIdInActiveCart(int id) {
        return null;
    }

}
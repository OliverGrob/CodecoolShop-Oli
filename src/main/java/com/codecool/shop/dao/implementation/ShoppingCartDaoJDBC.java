package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.ShoppingCartStatus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCartDaoJDBC implements ShoppingCartDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartDaoJDBC instance = null;


    public static ShoppingCartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoJDBC();
        }
        return instance;
    }

    private List<ShoppingCart> executeQueryWithReturnValue(String query) {
        List<ShoppingCart> resultList = new ArrayList<>();

        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                ShoppingCart data = new ShoppingCart(resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDate("time"),
                        ShoppingCartStatus.valueOf(resultSet.getString("status")));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void add(int userId, Date time, ShoppingCartStatus status) {
        controller.executeQuery(
            "INSERT INTO shopping_cart (id, user_id, time, status) " +
                "VALUES (DEFAULT, " + userId + ", " + time + ", '" + status.toString() + "');"
        );
    }

    @Override
    public ShoppingCart find(int id) {
        List<ShoppingCart> shoppingCarts = executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart WHERE id = '" + id + "';"
        );

        return (shoppingCarts.size() != 0) ? shoppingCarts.get(0) : null;
    }

    @Override
    public ShoppingCart findActiveCart() {
        List<ShoppingCart> shoppingCarts = executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart WHERE status LIKE 'IN_CART';"
        );

        return (shoppingCarts.size() != 0) ? shoppingCarts.get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
            "DELETE FROM shopping_cart WHERE id = '" + id + "';"
        );
    }

    @Override
    public List<ShoppingCart> getAll() {
        return executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart;"
        );
    }

    @Override
    public float calculateTotalPrice(List<Product> allProductsInCart) {
        float totalPrice = 0;

        for (Product product : allProductsInCart) {
            totalPrice += product.getDefaultPrice();
        }

        return totalPrice;
    }

}
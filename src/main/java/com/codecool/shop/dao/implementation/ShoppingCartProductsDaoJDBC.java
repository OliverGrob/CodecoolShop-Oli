package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.ShoppingCartProduct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartProductsDaoJDBC implements ShoppingCartProductsDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartDaoJDBC instance = null;


    public static ShoppingCartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoJDBC();
        }
        return instance;
    }

    private List<ShoppingCartProduct> executeQueryWithReturnValue(String query) {
        List<ShoppingCartProduct> resultList = new ArrayList<>();

        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                ShoppingCartProduct data = new ShoppingCartProduct(resultSet.getInt("shopping_cart_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("amount"));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private List<ShoppingCartProduct> findProduct(int shoppingCartId, int productId) {
        return executeQueryWithReturnValue(
                "SELECT * FROM shopping_cart_products" +
                        "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
        );
    }

    @Override
    public void addProductToShoppingCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> shoppingCartProduct = this.findProduct(shoppingCartId, productId);

        if (shoppingCartProduct.size() == 0) {
            controller.executeQuery(
                "INSERT INTO shopping_cart_products (shopping_cart_id, product_id, amount)" +
                    "VALUES (" + shoppingCartId + ", " + productId + ", 1);"
            );
        } else {
            controller.executeQuery(
                "UPDATE shopping_cart_products SET amount = " + (shoppingCartProduct.get(0).getAmount() + 1) + " " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        }
    }

    @Override
    public void removeProductFromShoppingCart(int shoppingCartId, int productId) {
        int productAmount = this.findProduct(shoppingCartId, productId).get(0).getAmount();

        if (productAmount == 0) {
            controller.executeQuery(
                "DELETE FROM shopping_cart_products" +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        } else {
            controller.executeQuery(
                "UPDATE shopping_cart_products SET amount = " + (productAmount - 1) + " " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        }
    }

    @Override
    public List<ShoppingCartProduct> getAll() {
        return executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart_products;"
        );
    }

}

package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCartProduct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ShoppingCartProductsDaoJDBC implements ShoppingCartProductsDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartProductsDaoJDBC instance = null;


    public static ShoppingCartProductsDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartProductsDaoJDBC();
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
            "SELECT * FROM shopping_cart_products " +
                "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
        );
    }

    @Override
    public void addProductToShoppingCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> shoppingCartProduct = this.findProduct(shoppingCartId, productId);

        if (shoppingCartProduct.size() == 0) {
            controller.executeQueryNotPreparedStatement(
                "INSERT INTO shopping_cart_products (shopping_cart_id, product_id, amount) " +
                    "VALUES (" + shoppingCartId + ", " + productId + ", 1);"
            );
        } else {
            controller.executeQueryNotPreparedStatement(
                "UPDATE shopping_cart_products SET amount = " + (shoppingCartProduct.get(0).getAmount() + 1) + " " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        }
    }

    @Override
    public void removeProductFromShoppingCart(int shoppingCartId, int productId) {
        int productAmount = this.findProduct(shoppingCartId, productId).get(0).getAmount();

        if (productAmount == 1) {
            controller.executeQueryNotPreparedStatement(
                "DELETE FROM shopping_cart_products " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        } else {
            controller.executeQueryNotPreparedStatement(
                "UPDATE shopping_cart_products SET amount = " + (productAmount - 1) + " " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
            );
        }
    }

    @Override
    public List<ShoppingCartProduct> getShoppingCartProductsByShoppingCartId(int shoppingCartId) {
        return executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart_products " +
                "WHERE shopping_cart_id = '" + shoppingCartId + "' ORDER BY product_id;"
        );
    }

    @Override
    public List<ShoppingCartProduct> getAll() {
        return executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart_products;"
        );
    }

    @Override
    public Integer getProductQuantityByProductIdInActiveCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> products =  executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart_products " +
                "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + productId + "';"
        );

        return (products.size() != 0) ? products.get(0).getAmount() : 0;
    }

    @Override
    public Map<Integer, Integer> getAllProductQuantity(int shoppingCartId, Set<Product> products) {
        Map<Integer, Integer> productsInCart = new HashMap<>();

        for (Product product : products) {
            productsInCart.put(product.getId(), executeQueryWithReturnValue(
                "SELECT * FROM shopping_cart_products " +
                    "WHERE shopping_cart_id = '" + shoppingCartId + "' AND product_id = '" + product.getId() + "';"
            ).get(0).getAmount());
        }

        return productsInCart;
    }

}

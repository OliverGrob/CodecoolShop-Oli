package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCartProduct;

import java.sql.*;
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

    private List<ShoppingCartProduct> executeQueryWithReturnValue(String query, List<Object> parameters) {
        Connection connection = controller.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ShoppingCartProduct> resultList = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ShoppingCartProduct data = new ShoppingCartProduct(resultSet.getInt("shopping_cart_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("amount"));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (preparedStatement != null) preparedStatement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return resultList;
    }

    private List<ShoppingCartProduct> findProduct(int shoppingCartId, int productId) {
        return executeQueryWithReturnValue(
        "SELECT * FROM shopping_cart_products " +
                "WHERE shopping_cart_id = ? AND product_id = ?;",
            Arrays.asList(shoppingCartId, productId));
    }

    @Override
    public void addProductToShoppingCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> shoppingCartProduct = this.findProduct(shoppingCartId, productId);

        if (shoppingCartProduct.size() == 0) {
            controller.executeQuery(
            "INSERT INTO shopping_cart_products (shopping_cart_id, product_id, amount) " +
                    "VALUES (?, ?, 1);",
                Arrays.asList(shoppingCartId, productId));
        } else {
            controller.executeQuery(
            "UPDATE shopping_cart_products SET amount = (? + 1) " +
                    "WHERE shopping_cart_id = ? AND product_id = ?;",
                Arrays.asList(shoppingCartProduct.get(0).getAmount(), shoppingCartId, productId));
        }
    }

    @Override
    public void removeProductFromShoppingCart(int shoppingCartId, int productId) {
        int productAmount = this.findProduct(shoppingCartId, productId).get(0).getAmount();

        if (productAmount == 1) {
            controller.executeQuery(
            "DELETE FROM shopping_cart_products " +
                    "WHERE shopping_cart_id = ? AND product_id = ?;",
                Arrays.asList(shoppingCartId, productId));
        } else {
            controller.executeQuery(
            "UPDATE shopping_cart_products SET amount = (? - 1) " +
                    "WHERE shopping_cart_id = ? AND product_id = ?;",
                Arrays.asList(productAmount, shoppingCartId, productId));
        }
    }

    @Override
    public List<ShoppingCartProduct> getShoppingCartProductsByShoppingCartId(int shoppingCartId) {
        return executeQueryWithReturnValue(
        "SELECT * FROM shopping_cart_products " +
            "WHERE shopping_cart_id = ? ORDER BY product_id;",
            Collections.singletonList(shoppingCartId));
    }

    @Override
    public List<ShoppingCartProduct> getAll() {
        return executeQueryWithReturnValue(
        "SELECT * FROM shopping_cart_products;",
            Collections.emptyList());
    }

    @Override
    public Integer getProductQuantityByProductIdInActiveCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> products =  executeQueryWithReturnValue(
        "SELECT * FROM shopping_cart_products " +
                "WHERE shopping_cart_id = ? AND product_id = ?;",
            Arrays.asList(shoppingCartId, productId));

        return (products.size() != 0) ? products.get(0).getAmount() : 0;
    }

    @Override
    public Map<Integer, Integer> getAllProductQuantity(int shoppingCartId, Set<Product> products) {
        Map<Integer, Integer> productsInCart = new HashMap<>();

        for (Product product : products) {
            productsInCart.put(product.getId(), executeQueryWithReturnValue(
            "SELECT * FROM shopping_cart_products " +
                    "WHERE shopping_cart_id = ? AND product_id = ?;",
                Arrays.asList(shoppingCartId, product.getId())
            ).get(0).getAmount());
        }

        return productsInCart;
    }

}

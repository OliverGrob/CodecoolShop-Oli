package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.ShoppingCart;
import com.codecool.shop.model.ShoppingCartStatus;
import com.codecool.shop.model.User;

import java.util.*;
import java.util.Date;

public class ShoppingCartDaoJDBC implements ShoppingCartDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartDaoJDBC instance = null;


    public static ShoppingCartDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoJDBC();
        }
        return instance;
    }

    private List<ShoppingCart> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<ShoppingCart> shoppingCarts = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            shoppingCarts.add(new ShoppingCart((Integer) singleRow.get("id"),
                    new User((Integer) singleRow.get("user_id"),
                            (String) singleRow.get("email_address"),
                            (String) singleRow.get("password"),
                            (String) singleRow.get("first_name"),
                            (String) singleRow.get("last_name"),
                            (String) singleRow.get("country"),
                            (String) singleRow.get("city"),
                            (String) singleRow.get("address"),
                            (String) singleRow.get("zip_code"),
                            (Boolean) singleRow.get("is_shipping_same")),
                    (Date) singleRow.get("time"),
                    ShoppingCartStatus.valueOf((String) singleRow.get("status"))));
        }

        return shoppingCarts;
    }

    @Override
    public void add(int userId, Date time) {
        controller.executeQuery(
        "INSERT INTO shopping_cart (id, user_id, time, status) " +
                "VALUES (DEFAULT, ?, ?, ?);",
            Arrays.asList(userId, time, ShoppingCartStatus.IN_CART.toString()));
    }

    @Override
    public ShoppingCart find(int shoppingCartId) {
        List<Map<String, Object>> shoppingCarts = controller.executeQueryWithReturnValue(
        "SELECT shopping_cart.id, users.id AS user_id, users.email_address, users.password, users.first_name, " +
                  "users.last_name, users.country, users.city, users.address, users.zip_code, users.is_shipping_same, " +
                  "shopping_cart.time, shopping_cart.status " +
                "FROM shopping_cart " +
                  "JOIN users ON shopping_cart.user_id = users.id " +
                "WHERE shopping_cart.id = ?;",
            Collections.singletonList(shoppingCartId));

        return (shoppingCarts.size() != 0) ? this.objectCreator(shoppingCarts).get(0) : null;
    }

    @Override
    public ShoppingCart findActiveCartForUser(int userId) {
        List<Map<String, Object>> shoppingCarts = controller.executeQueryWithReturnValue(
        "SELECT shopping_cart.id, users.id AS user_id, users.email_address, users.password, users.first_name, " +
                  "users.last_name, users.country, users.city, users.address, users.zip_code, users.is_shipping_same, " +
                  "shopping_cart.time, shopping_cart.status " +
                "FROM shopping_cart " +
                  "JOIN users ON shopping_cart.user_id = users.id " +
                "WHERE shopping_cart.user_id = ? AND shopping_cart.status LIKE 'IN_CART';",
            Collections.singletonList(userId));

        return (shoppingCarts.size() != 0) ? this.objectCreator(shoppingCarts).get(0) : null;
    }

    @Override
    public void changeCartStatus(int userId, int shoppingCartId, ShoppingCartStatus changeStatusTo) {
        controller.executeQuery(
        "UPDATE shopping_cart SET status = ? " +
                "WHERE shopping_cart.id = ? AND shopping_cart.user_id = ?;",
            Arrays.asList(changeStatusTo.toString(), shoppingCartId, userId));
    }

    @Override
    public void remove(int shoppingCartId) {
        controller.executeQuery(
        "DELETE FROM shopping_cart WHERE id = ?;",
            Collections.singletonList(shoppingCartId));
    }

    @Override
    public List<Map<String, Object>> getNonActiveShoppingCartsForUser(int userId) {
        return controller.executeQueryWithReturnValue(
        "SELECT shopping_cart.id, shopping_cart.time, shopping_cart.status, " +
                  "SUM(shopping_cart_products.amount) AS amount, " +
                  "SUM(product.default_price * shopping_cart_products.amount) AS price " +
                "FROM shopping_cart " +
                  "JOIN shopping_cart_products ON shopping_cart.id = shopping_cart_products.shopping_cart_id " +
                  "JOIN product ON shopping_cart_products.product_id = product.id " +
                "WHERE shopping_cart.user_id = ? AND shopping_cart.status NOT LIKE 'IN_CART' " +
                "GROUP BY shopping_cart.id, shopping_cart.time, shopping_cart.status;",
            Collections.singletonList(userId));
    }

}
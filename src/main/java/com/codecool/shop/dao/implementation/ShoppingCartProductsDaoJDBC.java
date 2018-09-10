package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShoppingCartProductsDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.*;

import java.util.*;
import java.util.Date;

public class ShoppingCartProductsDaoJDBC implements ShoppingCartProductsDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShoppingCartProductsDaoJDBC instance = null;


    public static ShoppingCartProductsDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShoppingCartProductsDaoJDBC();
        }
        return instance;
    }

    private List<ShoppingCartProduct> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<ShoppingCartProduct> shoppingCartProducts = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            shoppingCartProducts.add(new ShoppingCartProduct(new ShoppingCart((Integer) singleRow.get("shop_cart_id"),
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
                    ShoppingCartStatus.valueOf((String) singleRow.get("status"))),
                    new Product((Integer) singleRow.get("id"),
                            (String) singleRow.get("name"),
                            (float) ((double) singleRow.get("default_price")),
                            (String) singleRow.get("currency_string"),
                            (String) singleRow.get("description"),
                            new ProductCategory((Integer) singleRow.get("prod_cat_id"),
                                    (String) singleRow.get("prod_cat_name"),
                                    (String) singleRow.get("prod_cat_desc"),
                                    (String) singleRow.get("prod_cat_dep")),
                            new Supplier((Integer) singleRow.get("supp_id"),
                                    (String) singleRow.get("supp_name"),
                                    (String) singleRow.get("supp_desc"))),
                    (Integer) singleRow.get("amount")));
        }

        return shoppingCartProducts;
    }

    private List<ShoppingCartProduct> findProduct(int shoppingCartId, int productId) {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT product.id, product.name, product.default_price, product.currency_string, product.description, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.description AS prod_cat_desc, " +
                  "product_category.department AS prod_cat_dep, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc, " +
                  "shopping_cart.id AS shop_cart_id, users.id AS user_id, users.email_address, users.password, users.first_name, " +
                  "users.last_name, users.country, users.city, users.address, users.zip_code, users.is_shipping_same, " +
                  "shopping_cart.time, shopping_cart.status, " +
                  "shopping_cart_products.amount " +
                "FROM shopping_cart_products " +
                  "JOIN product ON shopping_cart_products.product_id = product.id " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                  "JOIN shopping_cart ON shopping_cart_products.shopping_cart_id = shopping_cart.id " +
                  "JOIN users ON shopping_cart.user_id = users.id " +
                "WHERE shopping_cart.id = ? AND product.id = ?;",
            Arrays.asList(shoppingCartId, productId)));
    }

    @Override
    public int addProductToShoppingCart(int shoppingCartId, int productId) {
        List<ShoppingCartProduct> products = findProduct(shoppingCartId, productId);
        int newAmount;

        if (products.size() == 0) {
            newAmount = (Integer) controller.executeQueryWithReturnValue(
            "INSERT INTO shopping_cart_products (shopping_cart_id, product_id, amount) " +
                    "VALUES (?, ?, 1) " +
                    "RETURNING amount;",
                Arrays.asList(shoppingCartId, productId)).get(0).get("amount");
        } else {
            newAmount = (Integer) controller.executeQueryWithReturnValue(
            "UPDATE shopping_cart_products SET amount = (? + 1) " +
                    "WHERE shopping_cart_id = ? AND product_id = ? " +
                    "RETURNING amount;",
                Arrays.asList(products.get(0).getAmount(), shoppingCartId, productId)).get(0).get("amount");
        }

        return newAmount;
    }

    @Override
    public int removeProductFromShoppingCart(int shoppingCartId, int productId) {
        int productAmount = findProduct(shoppingCartId, productId).get(0).getAmount();
        int newAmount;

        if (productAmount == 1) {
            newAmount = (Integer) controller.executeQueryWithReturnValue(
            "DELETE FROM shopping_cart_products " +
                    "WHERE shopping_cart_id = ? AND product_id = ? " +
                    "RETURNING 0 AS amount;",
                Arrays.asList(shoppingCartId, productId)).get(0).get("amount");
        } else {
            newAmount = (Integer) controller.executeQueryWithReturnValue(
            "UPDATE shopping_cart_products SET amount = (? - 1) " +
                    "WHERE shopping_cart_id = ? AND product_id = ? " +
                    "RETURNING amount;",
                Arrays.asList(productAmount, shoppingCartId, productId)).get(0).get("amount");
        }
        return newAmount;
    }

    @Override
    public List<ShoppingCartProduct> getShoppingCartProductsByUser(int userId) {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT product.id, product.name, product.default_price, product.currency_string, product.description, " +
                  "product_category.id AS prod_cat_id, " +
                  "product_category.name AS prod_cat_name, " +
                  "product_category.description AS prod_cat_desc, " +
                  "product_category.department AS prod_cat_dep, " +
                  "supplier.id AS supp_id, " +
                  "supplier.name AS supp_name, " +
                  "supplier.description AS supp_desc, " +
                  "shopping_cart.id AS shop_cart_id, users.id AS user_id, users.email_address, users.password, users.first_name, " +
                  "users.last_name, users.country, users.city, users.address, users.zip_code, users.is_shipping_same, " +
                  "shopping_cart.time, shopping_cart.status, " +
                  "shopping_cart_products.amount " +
                "FROM shopping_cart_products " +
                  "JOIN product ON shopping_cart_products.product_id = product.id " +
                  "JOIN product_category ON product.product_category_id = product_category.id " +
                  "JOIN supplier ON product.supplier_id = supplier.id " +
                  "JOIN shopping_cart ON shopping_cart_products.shopping_cart_id = shopping_cart.id " +
                  "JOIN users ON shopping_cart.user_id = users.id " +
                "WHERE users.id = ? AND shopping_cart.status LIKE 'IN_CART' " +
                "ORDER BY product.id;",
            Collections.singletonList(userId)));
    }

    @Override
    public int calculateTotalItemNumber(List<ShoppingCartProduct> shoppingCartProducts) {
        return shoppingCartProducts.stream().mapToInt(ShoppingCartProduct::getAmount).sum();
    }

    @Override
    public float calculateTotalPrice(List<ShoppingCartProduct> shoppingCartProducts) {
        return (float) shoppingCartProducts.stream().mapToDouble(product -> product.getProduct().getDefaultPrice()).sum();
    }

}

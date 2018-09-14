package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ShippingAddressDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.ShippingAddress;
import com.codecool.shop.model.User;

import java.util.*;

public class ShippingAddressDaoJDBC implements ShippingAddressDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ShippingAddressDaoJDBC instance = null;


    public static ShippingAddressDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShippingAddressDaoJDBC();
        }
        return instance;
    }

    private List<ShippingAddress> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<ShippingAddress> users = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            users.add(new ShippingAddress(new User((Integer) singleRow.get("id"),
                                (String) singleRow.get("email_address"),
                                (String) singleRow.get("password"),
                                (String) singleRow.get("first_name"),
                                (String) singleRow.get("last_name"),
                                (String) singleRow.get("country_user"),
                                (String) singleRow.get("city_user"),
                                (String) singleRow.get("address_user"),
                                (String) singleRow.get("zip_code_user"),
                                (Boolean) singleRow.get("is_shipping_same")),
                    (String) singleRow.get("country"),
                    (String) singleRow.get("city"),
                    (String) singleRow.get("address"),
                    (String) singleRow.get("zip_code")));
        }

        return users;
    }

    @Override
    public void addShippingAddress(int userId, String address, String country, String city, String zipCode) {
        if (this.isNewShippingAddress(userId, address, country, city, zipCode)) {
            controller.executeQuery(
            "INSERT INTO shipping_address " +
                    "VALUES (?, ?, ?, ?, ?);",
                Arrays.asList(userId, country, city, address, zipCode));
        }
    }

    @Override
    public ShippingAddress find(int userId) {
        List<Map<String, Object>> shippingAddresses = controller.executeQueryWithReturnValue(
        "SELECT users.id, users.email_address, users.password, users.first_name, users.last_name, " +
                  "users.country AS country_user, " +
                  "users.city AS city_user, " +
                  "users.address AS address_user, " +
                  "users.zip_code AS zip_code_user, " +
                  "users.is_shipping_same, " +
                  "shipping_address.country, " +
                  "shipping_address.city, " +
                  "shipping_address.address, " +
                  "shipping_address.zip_code " +
                "FROM shipping_address " +
                  "JOIN users ON shipping_address.user_id = users.id " +
                "WHERE shipping_address.user_id = ?;",
            Collections.singletonList(userId));

        return (shippingAddresses.size() != 0) ? this.objectCreator(shippingAddresses).get(0) : null;
    }

    private boolean isNewShippingAddress(int userId, String address, String country, String city, String zipCode) {
        ShippingAddress shippingAddress = this.find(userId);

        return address.equals(shippingAddress.getAddress()) && country.equals(shippingAddress.getCountry()) &&
                city.equals(shippingAddress.getCity()) && zipCode.equals(shippingAddress.getZipCode());
    }

}

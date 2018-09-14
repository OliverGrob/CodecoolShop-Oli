package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class UserDaoJDBC implements UserDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static UserDaoJDBC instance = null;


    public static UserDaoJDBC getInstance() {
        if (instance == null) {
            instance = new UserDaoJDBC();
        }
        return instance;
    }

    private List<User> objectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<User> users = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            users.add(new User((Integer) singleRow.get("id"),
                    (String) singleRow.get("email_address"),
                    (String) singleRow.get("password"),
                    (String) singleRow.get("first_name"),
                    (String) singleRow.get("last_name"),
                    (String) singleRow.get("country"),
                    (String) singleRow.get("city"),
                    (String) singleRow.get("address"),
                    (String) singleRow.get("zip_code"),
                    (Boolean) singleRow.get("is_shipping_same")));
        }

        return users;
    }

    @Override
    public void add(String emailAddress, String password) {
        controller.executeQuery(
        "INSERT INTO users (id, email_address, password, first_name, last_name, country, city, address, zip_code, is_shipping_same) " +
                "VALUES (DEFAULT, ?, ?, '',  '', '', '', '', '', false);",
            Arrays.asList(emailAddress, password));
    }

    @Override
    public void add(String emailAddress, String password, String firstName, String lastName, String country,
                    String city, String address, String zipCode, boolean isShippingSame) {
        controller.executeQuery(
        "INSERT INTO users (id, email_address, password, first_name, last_name, country, " +
                "city, address, zip_code, is_shipping_same) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
            Arrays.asList(emailAddress, password, firstName, lastName, country, city, address, zipCode, isShippingSame));
    }

    @Override
    public void setBillingAddress(int userId, String firstName, String lastName, String country,
                                  String city, String address, String zipCode, Boolean isShippingSame) {
        controller.executeQuery(
        "UPDATE users SET first_name = ?, last_name = ?, country = ?, city = ?, address = ?, zip_code = ?, is_shipping_same = ? " +
                "WHERE users.id = ?;",
            Arrays.asList(firstName, lastName, country, city, address, zipCode, isShippingSame, userId));
    }

    @Override
    public User find(int id) {
        List<Map<String, Object>> users = controller.executeQueryWithReturnValue(
        "SELECT * FROM users WHERE id = ?;",
            Collections.singletonList(id));

        return (users.size() != 0) ? this.objectCreator(users).get(0) : null;
    }

    @Override
    public User find(String email) {
        List<Map<String, Object>> users = controller.executeQueryWithReturnValue(
        "SELECT * FROM users WHERE email_address LIKE ?;",
            Collections.singletonList(email));

        return (users.size() != 0) ? this.objectCreator(users).get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM users WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<User> getAll() {
        return this.objectCreator(controller.executeQueryWithReturnValue(
        "SELECT * FROM users;",
            Collections.emptyList()));
    }

    private boolean isEmailValid(String email) {
        return this.getAll()
                .stream()
                .filter(user -> user.getEmailAddress().equals(email))
                .collect(Collectors.toList()).size() == 0;
    }

    @Override
    public boolean validRegister(String email, String password, String passwordConfirm) {
        return isEmailValid(email) && password.equals(passwordConfirm);
    }

    @Override
    public boolean validLogin(String email, String password) {
        User user = this.find(email);

        return (user != null) && (user.getEmailAddress().equals(email) && user.getPassword().equals(password));
    }

}

package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDaoJDBC implements UserDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static ProductDaoJDBC instance = null;


    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    private List<User> executeQueryWithReturnValue(String query, List<Object> parameters) {
        Connection connection = controller.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> resultList = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User data = new User(resultSet.getInt("id"),
                        resultSet.getString("email_address"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("address"),
                        resultSet.getString("zip_code"),
                        resultSet.getBoolean("is_shipping_same"));
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

    @Override
    public void add(String emailAddress, String password) {
        controller.executeQuery(
        "INSERT INTO users (id, email_address, password, first_name, last_name, country, city, address, zip_code, is_shipping_same) " +
                "VALUES (DEFAULT, ?, ?, 'None',  'None', 'None', 'None', 'None', 'None', false);",
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
    public User find(int id) {
        List<User> users = executeQueryWithReturnValue(
        "SELECT * FROM users WHERE id = ?;",
            Collections.singletonList(id));

        return (users.size() != 0) ? users.get(0) : null;
    }

    @Override
    public User find(String email) {
        List<User> users = executeQueryWithReturnValue(
        "SELECT * FROM users WHERE email_address LIKE ?;",
            Collections.singletonList(email));

        return (users.size() != 0) ? users.get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM users WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<User> getAll() {
        return executeQueryWithReturnValue(
        "SELECT * FROM users;",
            Collections.emptyList());
    }

}

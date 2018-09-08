package com.codecool.shop.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCController {

    private static final String DATABASE = "jdbc:postgresql://" + System.getenv("MY_PSQL_HOST") + ":5432/" + System.getenv("MY_PSQL_DBNAME");
    private static final String DB_USER = System.getenv("MY_PSQL_USER");
    private static final String DB_PASSWORD = System.getenv("MY_PSQL_PASSWORD");
    private static JDBCController instance = null;

 
    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    DATABASE,
                    DB_USER,
                    DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static JDBCController getInstance() {
        if (instance == null) {
            instance = new JDBCController();
        }
        return instance;
    }

    public void executeQuery(String query, List<Object> parameters) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (preparedStatement != null) preparedStatement.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public List<Map<String, Object>> executeQueryWithReturnValue(String query, List<Object> parameters) {
        Connection connection = this.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            resultSet = preparedStatement.executeQuery();

            int numOfCols = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> data = new HashMap<>();

                for (int i = 0; i < numOfCols; i++) {
                    data.put(resultSet.getMetaData().getColumnName(i + 1),
                             resultSet.getObject(i + 1));
                }

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

}
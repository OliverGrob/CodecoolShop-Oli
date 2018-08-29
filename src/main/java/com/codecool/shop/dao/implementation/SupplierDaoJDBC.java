package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Supplier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static SupplierDaoJDBC instance = null;


    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    private List<Supplier> executeQueryWithReturnValue(String query) {
        List<Supplier> resultList = new ArrayList<>();

        try (Connection connection = controller.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Supplier data = new Supplier(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"));
                resultList.add(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public void add(String name, String description) {
        controller.executeQuery(
            "INSERT INTO supplier(id, name, description) " +
                "VALUES (DEFAULT, '" + name + "', '" + description + "');"
        );
    }

    @Override
    public Supplier find(int id) {
        List<Supplier> suppliers = executeQueryWithReturnValue(
            "SELECT * FROM supplier WHERE id = '" + id + "';"
        );

        return (suppliers.size() != 0) ? suppliers.get(0) : null;
    }

    @Override
    public Supplier find(String name) {
        List<Supplier> suppliers = executeQueryWithReturnValue(
            "SELECT * FROM supplier WHERE name LIKE '" + name + "';"
        );

        return (suppliers.size() != 0) ? suppliers.get(0) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
            "DELETE FROM supplier WHERE id = '" + id + "';"
        );
    }

    @Override
    public List<Supplier> getAll() {
        return executeQueryWithReturnValue(
            "SELECT * FROM supplier;"
        );
    }

}
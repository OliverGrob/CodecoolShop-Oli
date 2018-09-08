package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.jdbc.JDBCController;
import com.codecool.shop.model.Supplier;

import java.util.*;

public class SupplierDaoJDBC implements SupplierDao {

    private static final JDBCController controller = JDBCController.getInstance();
    private static SupplierDaoJDBC instance = null;


    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    private Supplier singleObjectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        Map<String, Object> singleRow = resultRowsFromQuery.get(0);

        return new Supplier((Integer) singleRow.get("id"),
                (String) singleRow.get("name"),
                (String) singleRow.get("description"));
    }

    private List<Supplier> multipleObjectCreator(List<Map<String,Object>> resultRowsFromQuery) {
        List<Supplier> productCategories = new ArrayList<>();

        for (Map singleRow : resultRowsFromQuery) {
            productCategories.add(new Supplier((Integer) singleRow.get("id"),
                    (String) singleRow.get("name"),
                    (String) singleRow.get("description")));
        }

        return productCategories;
    }

    @Override
    public void add(String name, String description) {
        controller.executeQuery(
        "INSERT INTO supplier(id, name, description) " +
                "VALUES (DEFAULT, ?, ?);",
            Arrays.asList(name, description));
    }

    @Override
    public Supplier find(int id) {
        List<Map<String, Object>> suppliers = controller.executeQueryWithReturnValue(
        "SELECT * FROM supplier WHERE id = ?;",
            Collections.singletonList(id));

        return (suppliers.size() != 0) ? this.singleObjectCreator(suppliers) : null;
    }

    @Override
    public Supplier find(String name) {
        List<Map<String, Object>> suppliers = controller.executeQueryWithReturnValue(
        "SELECT * FROM supplier WHERE name LIKE ?;",
            Collections.singletonList(name));

        return (suppliers.size() != 0) ? this.singleObjectCreator(suppliers) : null;
    }

    @Override
    public void remove(int id) {
        controller.executeQuery(
        "DELETE FROM supplier WHERE id = ?;",
            Collections.singletonList(id));
    }

    @Override
    public List<Supplier> getAll() {
        return this.multipleObjectCreator(controller.executeQueryWithReturnValue(
        "SELECT * FROM supplier;",
            Collections.emptyList()));
    }

}
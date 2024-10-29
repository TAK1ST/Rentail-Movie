package base;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;
import static main.utils.Utility.extractNumber;

public abstract class CollectionManager<T extends Model> {

    protected final List<T> list = new ArrayList<>();
    protected boolean isNotSaved = false;
    private final String className;

    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "root";
    private static final String PASSWORD = "1";

    public CollectionManager(String tableName, String className) throws IOException {
        this.className = className;
        this.isNotSaved = false;
        loadData(tableName);
    }

    public abstract List<T> searchBy(String property);

//    public void log() {
//        list.forEach(System.out::println);
//    }

    public T getById(String message) {
        return searchById(getString(message, false));
    }

    public final void loadData(String tableName) throws IOException {
        list.clear();
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                T item = createInstanceFromResultSet(resultSet);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            errorLog("Error loading data from database: " + e.getMessage());
            throw new IOException("Failed to load data", e);
        }
    }

    public boolean saveData(String tableName) throws IOException {
        if (!isNotSaved) {
            System.out.printf("Already saved %s.\n", className);
            return false;
        }

        // get columns from the first list
        Object[] firstItemValues = list.isEmpty() ? null : list.get(0).getDatabaseValues();
        if (firstItemValues == null) {
            throw new IOException("No values available for database insertion.");
        }

        //dynamic query to save flexible
        String query = "INSERT INTO " + tableName + " VALUES (" +
                String.join(", ", Collections.nCopies(firstItemValues.length, "?")) + ")";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (T item : list) {
                Object[] values = item.getDatabaseValues();
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            errorLog("Error saving data to database: " + e.getMessage());
            throw new IOException("Failed to save data", e);
        }

        loadData(tableName);
        isNotSaved = false;
        return true;
    }

    public T searchById(String id) {
        for (T item : list) {
            if (item.getId() != null && item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void sortById() {
        list.sort((item1, item2) -> {
            long num1 = extractNumber(item1.getId());
            long num2 = extractNumber(item2.getId());
            return Long.compare(num1, num2);
        });
    }

    public boolean checkNull(T item) {
        if (item != null) {
            return false;
        }
        System.out.printf("\nNo %s's data.\n", className);
        return true;
    }

    public boolean checkEmpty(List<T> list) {
        if (!list.isEmpty()) {
            return false;
        }
        System.out.printf("\nNo %s's data.\n", className);
        return true;
    }

    public void setNotSave() {
        isNotSaved = true;
    }

    public boolean getSavingCondition() {
        return isNotSaved;
    }

    public List<T> getList() {
        return list;
    }

    protected abstract T createInstanceFromResultSet(ResultSet resultSet) throws SQLException;

    protected abstract void populatePreparedStatement(PreparedStatement preparedStatement, T item) throws SQLException;
}

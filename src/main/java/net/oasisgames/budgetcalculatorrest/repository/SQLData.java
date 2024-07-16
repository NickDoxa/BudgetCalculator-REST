package net.oasisgames.budgetcalculatorrest.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helpful class used to have easy access to data within a PostgreSQL database.
 *
 * @author Nick
 */
public class SQLData {

    private final String database;
    private final Connection connection;
    public SQLData(String database) throws SQLException {
        this.database = database;
        Connect.registerDriver();
        this.connection = new Connect().login();
    }

    private static class Connect {

        /**
         * Registers the driver class
         */
        private static void registerDriver() {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found... Register failed!");
            }
        }

        private static final String url = "jdbc:postgresql://localhost:5432/nickdoxa";
        private static final String user = "nd13";
        private static final String password = "starwars13";

        /**
         * Connect to the PostgreSQL server using the registered driver
         * @return Connection to the database
         * @throws SQLException If a database access error occurs
         */
        private Connection login() throws SQLException {
            return DriverManager.getConnection(url, user, password);
        }

    }

    /**
     * Get the database name as a string
     * @return Database Name
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Get the PostgreSQL connection
     * @return PostgreSQL Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get a columns data from a row with a matching primary key
     * @param table The table to pull from
     * @param primaryKey the primary key name
     * @param primaryKeyValue the primary value of the row to search
     * @param column the column to pull the data from
     * @return The column result from the query in the form of an Object.
     * If an SQLException is thrown, it will return null!
     */
    public Object getSpecificData(String table, String primaryKey, String primaryKeyValue,
                                  String column) {
        try {
            var query = "SELECT " + column + " FROM " + table + " WHERE " + primaryKey
                    + " = '" + primaryKeyValue + "'";
            try (Statement statement = getConnection().createStatement()) {
                var results = statement.executeQuery(query);
                if (results.next()) return results.getObject(column);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * Get a columns data from a row with a matching primary key
     * @param table The table to pull from
     * @param primaryKey the primary key name
     * @param primaryKeyValue the primary value of the row to search
     * @param column the column to pull the data from
     * @return The results from the query in the form of a ResultSet.
     * If an SQLException is thrown, it will return null!
     */
    public ResultSet getSpecificResultSet(String table, String primaryKey, String primaryKeyValue,
                                          String column) {
        try {
            var query = "SELECT " + column + " FROM " + table + " WHERE " + primaryKey
                    + " = '" + primaryKeyValue + "'";
            try (Statement statement = getConnection().createStatement()) {
                return statement.executeQuery(query);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the data from an entire column in a table
     * @param table the table to pull from
     * @param column the column of data to retrieve
     * @return ArrayList of all the objects in the ResultSet
     */
    public List<Object> getEntireColumn(String table, String column) {
        try {
            var query = "SELECT " + column + " FROM " + table;
            try (Statement statement = getConnection().createStatement()) {
                var results = statement.executeQuery(query);
                var output = new ArrayList<>();
                while (results.next()) {
                    output.add(results.getObject(column));
                }
                return output;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the data from an entire column in a table
     * @param table the table to pull from
     * @param column the column of data to retrieve
     * @return all column data in the form of a ResultSet
     */
    public ResultSet getEntireColumnResultSet(String table, String column) {
        try {
            var query = "SELECT " + column + " FROM " + table;
            try (Statement statement = getConnection().createStatement()) {
                return statement.executeQuery(query);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates or adds a piece of data to a specific row and column in a table
     * @param table the table to send data to
     * @param primaryKey the primary key of the table
     * @param primaryKeyValue the primary value of the row
     * @param column the column to insert into
     * @param columnValue the value to insert into the column
     * @return Returns true if succeeded, false if failed
     */
    public boolean updateDataInRow(String table, String primaryKey, String primaryKeyValue,
                                   String column, Object columnValue) {
        var query = "UPDATE " + table + " SET " + column + " = '" + columnValue + "' WHERE " +
                primaryKey + " = '" + primaryKeyValue + "'";
        try {
            try (Statement statement = getConnection().createStatement()) {
                statement.execute(query);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new row of data
     * @param table the table to insert into
     * @param columns a map where the keys are the names of the columns and the values are
     *                the column values to insert
     * @return Returns true if succeeded, false if failed
     */
    public boolean addRow(String table, Map<String, Object> columns) {
        String columnNames = "(" + columns.keySet().stream()
                .reduce((a, b) -> a + "," + b).orElseGet(() -> "") + ")";
        System.out.println(columnNames);
        String columnValues = "(" + columns.keySet().stream()
                .map(columns::get)
                .map(str -> "'" + str + "'")
                .reduce((a, b) -> a + "," + b).orElseGet(() -> "") + ")";
        System.out.println(columnValues);
        var query = "INSERT INTO " + table + columnNames + " VALUES " + columnValues;
        System.out.println(query);
        try {
            try (Statement statement = getConnection().createStatement()) {
                statement.execute(query);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a value already exists in a given column
     * @param table the table to check
     * @param column the column to search
     * @param value the value to search from the column
     * @return true if found, false if not
     */
    public boolean valueExists(String table, String column, String value) {
        var query = "SELECT " + column + " FROM " + table;
        try {
            try (Statement statement = getConnection().createStatement()) {
                var results = statement.executeQuery(query);
                while (results.next()) {
                    if (results.getString(column).equals(value)) return true;
                }
            }
            return false;
        } catch (SQLException e) {
            System.out.println("[ERROR] SQLException: " + e.getMessage());
            return false;
        }
    }
}

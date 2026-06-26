package app.bookstore.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

class ConnectionWrapper implements AutoCloseable {
    private final String userName;
    private final String password;
    private final String connectionString;
    private Connection connection;

    public ConnectionWrapper(String userName, String password, String connectionString) {
        this.userName = userName;
        this.password = password;
        this.connectionString = connectionString;
    }

    public ResultSet executeQuery(String sql) {
        try {
            var statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Error when executing SQL: " + sql, e);
        }
    }

    public int executeUpdateQuery(String sql){
        try{
            var statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Error when executing SQL: " + sql, e);
        }
    }

    public void executeCommit(){
        try{
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseException("Error when commit SQL procedure ", e);
        }
    }

    /**
     *
     * openConnection is set without AutoCommit.
     *
     */

    public ConnectionWrapper openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, userName, password);
            connection.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DatabaseException("Cannot open connection", e);
        }
        return this;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}

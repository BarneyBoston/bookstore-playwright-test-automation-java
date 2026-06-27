package app.bookstore.db.utils;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class ConnectionWrapper implements AutoCloseable {
    private final String userName;
    private final String password;
    private final String connectionString;
    @Getter
    private Connection connection;

    public ConnectionWrapper(String userName, String password, String connectionString) {
        this.userName = userName;
        this.password = password;
        this.connectionString = connectionString;
    }

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

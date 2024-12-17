package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance; // Singleton instance
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/game_stopper";
    private static final String USER = "root";
    private static final String PASSWORD = "GameStopper@123";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        connect();
    }

    // Ensure connection is always valid
    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create the database connection.");
        }
    }

    // Public method to get the single instance of DatabaseConnection
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Get the actual connection object, ensuring it's valid
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect(); // Reconnect if connection is closed
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to validate the database connection.", e);
        }
        return connection;
    }
}
package pl.mjurek.banking.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static Connection connection;

    public static Connection getConnection(String dbName) {
        if (connection == null) {
            String url = "jdbc:sqlite:.\\" + dbName;

            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }
}

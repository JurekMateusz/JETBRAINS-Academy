package pl.mjurek.banking.db;

import java.sql.*;

public class CreateDB {
    public static void createNewDatabase(String fileName) {
        try (Connection conn = ConnectionProvider.getConnection(fileName);
             Statement statement = conn.createStatement()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

                String creteTable = "create table card(" +
                        " id      INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " number  TEXT," +
                        " pin     TEXT," +
                        " balance INTEGER DEFAULT 0);";
                statement.execute(creteTable);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
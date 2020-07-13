package pl.mjurek.banking.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDB {
    public static void createNewDatabase(String fileName) {
        Connection conn = ConnectionProvider.getConnection(fileName);
        try (Statement statement = conn.createStatement()) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();

                String creteTable = "create table if not exists card(" +
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
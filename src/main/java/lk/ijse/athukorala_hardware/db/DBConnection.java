package lk.ijse.athukorala_hardware.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    final static String DB_URL = "jdbc:mysql://localhost:3306/athukorala_hardware";
    final static String DB_USER = "root";
    final static String DB_PASSWORD = "mysql";


    private static DBConnection dbc;
    private Connection conn;
    private DBConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
        if (dbc == null) {
            dbc = new DBConnection();
        }
        return dbc;
    }

    public Connection getConnection() throws SQLException {
        return conn;
    }
}

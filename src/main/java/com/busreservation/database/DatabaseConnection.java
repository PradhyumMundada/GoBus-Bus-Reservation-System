package com.busreservation.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Creates JDBC connections. Replace the placeholder locally; do not commit a real password. */
public final class DatabaseConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/bus_reservation";
    private static final String USER = "root";
    private static final String PASSWORD =
            System.getenv("GOBUS_DB_PASSWORD");

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            throw new SQLException("MySQL JDBC driver was not found. Run Maven to download dependencies.", exception);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

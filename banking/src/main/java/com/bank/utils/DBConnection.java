package com.bank.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/harishsmartbank";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Harsh@123";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                env("DB_URL", DEFAULT_URL),
                env("DB_USER", DEFAULT_USER),
                env("DB_PASSWORD", DEFAULT_PASSWORD)
        );
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}

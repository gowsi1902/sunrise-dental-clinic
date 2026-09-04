package com.sunrise.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {

    private static final DbConnection INSTANCE = new DbConnection();
    private final String url;
    private final String user;
    private final String password;

    private DbConnection() {
        Properties props = new Properties();
        try (InputStream in = openConfig()) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read db.properties", e);
        }
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password", "");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL driver missing", e);
        }
    }

    public static DbConnection getInstance() {
        return INSTANCE;
    }

    public Connection open() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static InputStream openConfig() {
        ClassLoader cl = DbConnection.class.getClassLoader();
        InputStream in = cl.getResourceAsStream("db.properties");
        if (in == null) {
            in = cl.getResourceAsStream("db.properties.example");
        }
        if (in == null) {
            throw new IllegalStateException("Copy db.properties.example to db.properties");
        }
        return in;
    }
}

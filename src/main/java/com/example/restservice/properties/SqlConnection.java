package com.example.restservice.properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
    public static Connection getMySQLConnection() throws SQLException, ClassNotFoundException {
        String hostName = SqlConnectionSettings.HOSTNAME;
        String dbName = SqlConnectionSettings.dbName;
        String userName = SqlConnectionSettings.userName;
        String password = SqlConnectionSettings.password;
        return getMySQLConnection(hostName, dbName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName, String userName, String password)
            throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName + "?characterEncoding=Cp1251";
        Connection conn = DriverManager.getConnection(connectionURL, userName, password);
        return conn;
    }
}
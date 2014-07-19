package ru.isa.chat.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private final String dbUrl;
    private final String user_name;
    private final String password;

    public DBManager(String url, String u, String p) {
        this.dbUrl = url;
        this.user_name = u;
        this.password = p;

    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(dbUrl, user_name, password);

        } catch (ClassNotFoundException e) {
            System.out.println("DB Manager: CNFE " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB Manager: SQLE " + e.getMessage());
        }
        return conn;
    }

}

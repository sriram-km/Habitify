package com.habitify;

import java.sql.*;

public class MysqlKeeper {

    Statement stmt;
    Connection con = null;

    public MysqlKeeper() {
        String mysqlPort = System.getProperty("mysql.port");
        String mysqlUserName = System.getProperty("mysql.username");
        String mysqlUserPassword = System.getProperty("mysql.userPassword");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:" + mysqlPort + "/habitify", mysqlUserName, mysqlUserPassword);
            stmt = con.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
        public ResultSet query (String query) {
            ResultSet rs = null;
            try {
                rs = stmt.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rs;
    }

    public boolean insertRow(String query) {
        try {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

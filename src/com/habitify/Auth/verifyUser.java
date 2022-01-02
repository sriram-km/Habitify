package com.habitify.Auth;

import com.habitify.MysqlKeeper;
import com.habitify.login.Auth.OneTimeAuth;

import java.sql.ResultSet;
import java.sql.SQLException;

public class verifyUser {
    MysqlKeeper mysqlKeeper = new MysqlKeeper();
    OneTimeAuth oneTimeAuth = new OneTimeAuth();

    public boolean verifyUser(String phoneNumber, String password) {
        boolean validUser = false;
        String queryFormat = "select secretKey from userCredentials where phoneNumber = %s;";
        String query = String.format(queryFormat, phoneNumber);
        ResultSet resultSet = mysqlKeeper.query(query);
        String secretKey = null;
        try {
            if (resultSet.next()) {
                secretKey = resultSet.getString(1);
            }
            if (secretKey != null){
                validUser = oneTimeAuth.validate(secretKey,password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return validUser;
    }


}

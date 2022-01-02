package com.habitify;


import com.habitify.login.Auth.OneTimeAuth;
import com.habitify.servlet.AUTHENTICATE_CONSTANTS;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Login {
    public HashMap verifyCredentials(String phoneNumber, String otp){
        HashMap result = new HashMap<String,Object>();
        MysqlKeeper mysqlKeeper = new MysqlKeeper();
        OneTimeAuth oneTimeAuth = new OneTimeAuth();
        String queryFormat = "select secretKey from userCredentials where phoneNumber = %s;";
        String query = String.format(queryFormat, phoneNumber);
        ResultSet resultSet = mysqlKeeper.query(query);
        String secretKey = null;
        try {
            if (resultSet.next()) {
                secretKey = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (secretKey == null){
            result.put(AUTHENTICATE_CONSTANTS.STATUS, AUTHENTICATE_CONSTANTS.LOGIN_STATUS.USER_NOT_EXIST);
        }
        else {
            if (oneTimeAuth.validate(secretKey,otp)){
                result.put(AUTHENTICATE_CONSTANTS.STATUS, AUTHENTICATE_CONSTANTS.LOGIN_STATUS.VALID);
                HabitifySession habitifySession = new HabitifySession(phoneNumber);
                result.put(AUTHENTICATE_CONSTANTS.SESSION,habitifySession);
            }
            else {
                result.put(AUTHENTICATE_CONSTANTS.STATUS, AUTHENTICATE_CONSTANTS.LOGIN_STATUS.INVALID);
            }
        }
        return result;
    }
}

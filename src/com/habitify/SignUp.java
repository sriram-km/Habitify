package com.habitify;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;

import com.habitify.login.Auth.OneTimeAuth;
import com.habitify.message.TwilioHelper;

public class SignUp {
    MysqlKeeper mysqlKeeper = new MysqlKeeper();
    TwilioHelper twilioHelper = new TwilioHelper();
    private final String MESSAGE_BUILDER = "Hi, there. This is OTP for signing up on Habitify.\n OTP: %s ";
    String phoneNumber;
    String secretKey;
    OneTimeAuth oneTimeAuth;
    String qrUrl;

    private String otp = null;
    private boolean isValidUser;

    public boolean canThisNumberSignup(String phoneNumber){
        this.phoneNumber = phoneNumber;
        String queryFormat = "select secretKey from userCredentials where phoneNumber = %s;";
        String query = String.format(queryFormat, phoneNumber);
        Boolean validUser = true;
        ResultSet resultSet = mysqlKeeper.query(query);
        String secretKey = null;
        try {
            if (resultSet.next()) {
                validUser = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isValidUser = validUser;
        return validUser;
    }

    public boolean sendOTP(){
        if (isValidUser) {
            String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));
            this.otp = otp;
            String message = String.format(MESSAGE_BUILDER, otp);
            return twilioHelper.sendMessage(phoneNumber, message);
        }else{
            return false;
        }
    }

    public boolean verifyOtp(String otp){
        if (isValidUser) {
            return this.otp.equals(otp);
        }
        else {
            return false;
        }
    }

    public boolean createUser(){
        if (isValidUser) {
            oneTimeAuth = new OneTimeAuth();
            secretKey = oneTimeAuth.generateSecretKey();
            String queryFormat = "INSERT INTO userCredentials (phoneNumber, secretKey) VALUES (\'%s\', \'%s\');";
            String query = String.format(queryFormat, phoneNumber, secretKey);
            return mysqlKeeper.insertRow(query);
        }
        else {
            return false;
        }
    }

    public String getQrUrl(){
        if (isValidUser) {
            qrUrl = oneTimeAuth.getQrURL(oneTimeAuth.getGoogleAuthenticatorBarCode(secretKey, phoneNumber, "Habitify"));
            return qrUrl;
        }
        else{
            return "";
        }
    }

    public static void main(String args[]){
        SignUp signUp = new SignUp();
        signUp.canThisNumberSignup("+91855000600");
    }


}

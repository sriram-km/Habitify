package com.habitify.servlet;

public class AUTHENTICATE_CONSTANTS {
    public static enum SIGNUP_MESSAGE {
        SENT(100), PROBLEM_SENDING_MESSAGE(101), USER_ALREADY_EXIST(102);

        private int numVal;

        SIGNUP_MESSAGE(int numVal) {
            this.numVal = numVal;
        }

        public int getValue() {
            return numVal;
        }
    }

    public static enum SIGNUP_OTP_MESSAGE {
        MATCH(100), INCORRECT(101), MATCH_UNABLE_TO_CREATE_USER(102);

        private int numVal;

        SIGNUP_OTP_MESSAGE(int numVal) {
            this.numVal = numVal;
        }

        public int getValue() {
            return numVal;
        }
    }

//    Login result keys
    public static final String STATUS = "status";
    public static final String SESSION = "session";

    public static enum LOGIN_STATUS {
        VALID(100), INVALID(101), USER_NOT_EXIST(102);

        private int numVal;

        LOGIN_STATUS(int numVal) {
            this.numVal = numVal;
        }

        public int getValue() {
            return numVal;
        }
    }
}

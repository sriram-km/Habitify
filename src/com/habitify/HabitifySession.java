package com.habitify;

public class HabitifySession {
    private String PHONENUMBER;
    public HabitifySession(String phoneNumber){
        PHONENUMBER = phoneNumber;
    }

    public String getPHONENUMBER() {
        return PHONENUMBER;
    }
}

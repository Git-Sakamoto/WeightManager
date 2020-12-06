package com.example.weightmanager.enums;

public enum PrefEnum {
    FILE_NAME("pref_weight_manager"),
    HOUR_OF_DAY("hour_of_day"),
    MINUTE("minute"),
    NOTIFICATION_RUN("notification_run"),
    FIRST_START("first_start"),
    TIME_NULL(99);

    private int value;

    PrefEnum(int value){
        this.value = value;
    }

    public int getInt(){
        return this.value;
    }

    private String text;
    //private final String text;

    PrefEnum(String text){
        this.text = text;
    }

    public String getString(){
        return this.text;
    }
}

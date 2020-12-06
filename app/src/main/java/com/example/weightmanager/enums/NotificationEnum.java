package com.example.weightmanager.enums;

public enum NotificationEnum {
    START_ALARM("com.example.weightmanager.StartAlarm");

    private final String text;
    NotificationEnum(String text){
        this.text = text;
    }
    public String getString(){
        return this.text;
    }
}

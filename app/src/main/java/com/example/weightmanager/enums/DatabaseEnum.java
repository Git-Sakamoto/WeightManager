package com.example.weightmanager.enums;

public enum DatabaseEnum {
    USER_COL_ID_INDEX(0),
    USER_COL_NAME_INDEX(1),
    USER_COL_HEIGHT_INDEX(2),
    USER_COL_WEIGHT_INDEX(3),
    USER_COL_TARGET_WEIGHT_INDEX(4),
    HISTORY_COL_ID_INDEX(0),
    HISTORY_COL_WEIGHT_INDEX(1),
    HISTORY_COL_DATE_INDEX(2),
    HISTORY_COL_TIME_INDEX(3);

    private final int value;

    DatabaseEnum(int value){
        this.value = value;
    }

    public int getInt(){
        return this.value;
    }
}

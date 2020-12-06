package com.example.weightmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.example.weightmanager.enums.NotificationEnum.START_ALARM;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()!=null) {
            NotificationController notificationController = new NotificationController();
            if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                notificationController.startAlarm(context, false);
            }else if(intent.getAction().equals(START_ALARM.getString())) {
                notificationController.showNotification(context);
                notificationController.startAlarm(context, true);
            }
        }
    }
}

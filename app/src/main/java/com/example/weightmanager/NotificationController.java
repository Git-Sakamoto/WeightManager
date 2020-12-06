package com.example.weightmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weightmanager.enums.PrefEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.weightmanager.enums.NotificationEnum.START_ALARM;
import static com.example.weightmanager.enums.PrefEnum.*;

public class NotificationController {
    static final int REQUEST_CODE = 1;
    static final int TIME_NULL = PrefEnum.TIME_NULL.getInt();

    public void showNotification(Context context){
        final String CHANNEL_ID = context.getString(R.string.app_name);
        final String CHANNEL_NAME = context.getString(R.string.app_name);

        NotificationCompat.Builder notification;

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String today = df.format(new Date());

        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.openDB();
        Cursor c = dbAdapter.selectHistory(today);

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME.getString(), context.MODE_PRIVATE);
        boolean notificationRun = pref.getBoolean(NOTIFICATION_RUN.getString(),false);
        //DBに未登録、通知設定が有効
        if(c.getCount() == 0 && notificationRun){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancelAll();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, android.app.NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.canShowBadge();
                channel.enableLights(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                channel.setShowBadge(true);
                notificationManager.createNotificationChannel(channel);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                notification = new NotificationCompat.Builder(context,CHANNEL_ID);
            }else{
                notification = new NotificationCompat.Builder(context);
            }
            notification.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
            notification.setContentTitle("体重の入力");
            notification.setContentText("今日の体重を測定し、登録しましょう");
            notification.setShowWhen(false);

            Intent intent = new Intent(context,MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            notification.setContentIntent(pendingIntent);
            notification.setAutoCancel(true);
            notificationManager.notify(0, notification.build());
        }
        dbAdapter.closeDB();
        c.close();
    }

    public void startAlarm(Context context,boolean setNextDayAlarm){
        SharedPreferences pref = context.getSharedPreferences(FILE_NAME.getString(), context.MODE_PRIVATE);
            int hourOfDay = pref.getInt(HOUR_OF_DAY.getString(),TIME_NULL);
            int minute = pref.getInt(MINUTE.getString(),TIME_NULL);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            if(setNextDayAlarm) {
                calendar.add(Calendar.DATE, 1);
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.setAction(START_ALARM.getString());

            PendingIntent pending = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            if (am != null) {
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                }else{
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                }
                //Toast.makeText(context, "通知を有効にしました", Toast.LENGTH_SHORT).show();
            }
    }

    public void cancelAlarm(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(START_ALARM.getString());
        PendingIntent pending = PendingIntent.getBroadcast(context,REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(isWorkingPending(context)){pending.cancel();}
        AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        if(am!=null){am.cancel(pending);}
        Toast.makeText(context,"通知を無効にしました",Toast.LENGTH_SHORT).show();
    }

    public boolean isWorkingPending(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(START_ALARM.getString());
        boolean isWorking = (PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null);
        return isWorking;
    }

}
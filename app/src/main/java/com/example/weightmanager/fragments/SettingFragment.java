package com.example.weightmanager.fragments;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.weightmanager.NotificationController;
import com.example.weightmanager.R;
import com.example.weightmanager.enums.PrefEnum;

import static com.example.weightmanager.enums.PrefEnum.*;



public class SettingFragment extends Fragment {
    CheckBox checkBoxNotification;
    Button buttonTimeChange;
    TextView textNotificationTime;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static final int TIME_NULL = PrefEnum.TIME_NULL.getInt();
    int hourOfDay,minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        checkBoxNotification = view.findViewById(R.id.checkbox_notification);
        textNotificationTime = view.findViewById(R.id.text_notification_time);
        buttonTimeChange = view.findViewById(R.id.button_time_change);

        NotificationController notificationController = new NotificationController();

        pref = getActivity().getSharedPreferences(FILE_NAME.getString(), getActivity().MODE_PRIVATE);
        editor = pref.edit();

        setViews();

        checkBoxNotification.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean check = checkBoxNotification.isChecked();
                if(check){
                    editor.putBoolean(NOTIFICATION_RUN.getString(),true);
                    editor.commit();
                    notificationController.startAlarm(getActivity(),false);
                }else{
                    editor.putBoolean(NOTIFICATION_RUN.getString(),false);
                    editor.commit();
                    notificationController.cancelAlarm(getActivity());
                }
            }
        });

        buttonTimeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hh, int mm) {
                        hourOfDay = hh;
                        minute= mm;
                        editor.putInt(HOUR_OF_DAY.getString(),hourOfDay);
                        editor.putInt(MINUTE.getString(),minute);
                        editor.commit();
                        textNotificationTime.setText(hourOfDay+"時"+minute+"分");
                        notificationController.startAlarm(getActivity(),false);
                    }
                };
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),onTimeSetListener, hourOfDay, minute, true);
                dialog.show();
            }
        });

    }

    void setViews(){
        boolean notificationRun = pref.getBoolean(NOTIFICATION_RUN.getString(),false);
        if(notificationRun){
            checkBoxNotification.setChecked(true);
        }

        hourOfDay = pref.getInt(HOUR_OF_DAY.getString(),TIME_NULL);
        minute = pref.getInt(MINUTE.getString(),TIME_NULL);
        textNotificationTime.setText(hourOfDay+"時"+minute+"分");
    }

}
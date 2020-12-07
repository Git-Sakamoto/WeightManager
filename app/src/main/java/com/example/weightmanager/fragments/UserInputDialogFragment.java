package com.example.weightmanager.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.weightmanager.DBAdapter;
import com.example.weightmanager.DecimalDigitsInputFilter;
import com.example.weightmanager.EditTextManager;
import com.example.weightmanager.NotificationController;
import com.example.weightmanager.R;

import static android.content.Context.MODE_PRIVATE;
import static com.example.weightmanager.enums.PrefEnum.FILE_NAME;
import static com.example.weightmanager.enums.PrefEnum.HOUR_OF_DAY;
import static com.example.weightmanager.enums.PrefEnum.MINUTE;
import static com.example.weightmanager.enums.PrefEnum.NOTIFICATION_RUN;


public class UserInputDialogFragment extends DialogFragment {
    EditText editName,editHeight,editWeight,editTargetWeight;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View inputView = layoutInflater.inflate(R.layout.dialog_user_input, null);

        findViews(inputView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("プロフィールの登録");
        builder.setMessage("全ての項目を入力してください\n登録情報は後からでも変更できます");
        builder.setView(inputView);
        builder.setPositiveButton("登録",null);
        builder.setNegativeButton("終了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        this.setCancelable(false);
        AlertDialog alertDialog = builder.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextManager editTextManager = new EditTextManager(getActivity());
                boolean textIsNotError = editTextManager.textErrorCheck(editName,editHeight,editWeight,editTargetWeight);
                if(textIsNotError){
                    String name = editName.getText().toString();
                    String height = editHeight.getText().toString();
                    String weight = editWeight.getText().toString();
                    String targetWeight = editTargetWeight.getText().toString();

                    DBAdapter dbAdapter = new DBAdapter(getActivity());
                    dbAdapter.openDB();
                    dbAdapter.insertUser(name,height,weight,targetWeight);
                    dbAdapter.closeDB();
                    alertDialog.dismiss();

                    SharedPreferences pref = getActivity().getSharedPreferences(FILE_NAME.getString(), MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    int hourOfDay = 6;
                    int minute = 0;
                    editor.putInt(HOUR_OF_DAY.getString(),hourOfDay);
                    editor.putInt(MINUTE.getString(),minute);
                    editor.putBoolean(NOTIFICATION_RUN.getString(),true);
                    editor.commit();

                    NotificationController notificationController = new NotificationController();
                    if(notificationController.isWorkingPending(getActivity())==false){
                        notificationController.startAlarm(getActivity(),false);
                    }
                }
            }
        });
        return  alertDialog;
    }

    void findViews(View view){
        editName = view.findViewById(R.id.dialog_edit_name);
        editHeight = view.findViewById(R.id.dialog_edit_height);
        editHeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        editWeight = view.findViewById(R.id.dialog_edit_weight);
        editWeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        editTargetWeight = view.findViewById(R.id.dialog_edit_target_weight);
        editTargetWeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
    }

}

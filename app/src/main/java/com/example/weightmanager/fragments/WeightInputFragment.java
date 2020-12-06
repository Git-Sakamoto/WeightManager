package com.example.weightmanager.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weightmanager.DBAdapter;
import com.example.weightmanager.DecimalDigitsInputFilter;
import com.example.weightmanager.EditTextManager;
import com.example.weightmanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.weightmanager.enums.DatabaseEnum.*;

public class WeightInputFragment extends Fragment implements View.OnClickListener {
    DBAdapter dbAdapter;
    Button buttonInsert,buttonEdit,buttonUpdate,buttonCancel;
    EditText editWeight;
    TextView textDate,textTime;
    String id,weight,date,time;
    EditTextManager editTextManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextManager = new EditTextManager(getActivity());
        findViews(view);
        buttonInsert.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        date = WeightInputFragmentArgs.fromBundle(getArguments()).getDate();
        if(date==null) {
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("yyyy/MM/dd");
            date = sdf.format(new Date());
            sdf = new SimpleDateFormat("HH時mm分");
            time = sdf.format(new Date());
        }
        textDate.setText(date + "の体重");

        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();
        Cursor c = dbAdapter.selectHistory(date);
        if(c.getCount() == 0){
            buttonInsert.setVisibility(View.VISIBLE);

            textTime.setText("未測定");
        }else{
            if(c.moveToFirst()) {
                buttonEdit.setVisibility(View.VISIBLE);

                id = c.getString(HISTORY_COL_ID_INDEX.getInt());
                weight = c.getString(HISTORY_COL_WEIGHT_INDEX.getInt());
                time = c.getString(HISTORY_COL_TIME_INDEX.getInt());

                editWeight.setText(weight);
                textTime.setText(time);

                editTextManager.stopEditing(editWeight);
            }
        }
        c.close();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_insert:
                onClickButtonInsert();
                break;
            case R.id.button_edit:
                editStart();
                break;
            case R.id.button_update:
                onClickButtonUpdate();
                break;
            case R.id.button_cancel:
                editStop();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDB();
    }

    void findViews(View view){
        textDate = view.findViewById(R.id.text_date);
        textTime = view.findViewById(R.id.text_time);
        editWeight = view.findViewById(R.id.edit_weight);
        editWeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        buttonInsert = view.findViewById(R.id.button_insert);
        buttonEdit = view.findViewById(R.id.button_edit);
        buttonUpdate = view.findViewById(R.id.button_update);
        buttonCancel = view.findViewById(R.id.button_cancel);
    }

    void editStart(){
        buttonEdit.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.VISIBLE);
        buttonUpdate.setVisibility(View.VISIBLE);

        editTextManager.startEditing(editWeight);
    }

    void editStop(){
        buttonEdit.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.GONE);
        buttonUpdate.setVisibility(View.GONE);

        editTextManager.stopEditing(editWeight);

        editWeight.setText(weight);
    }

    void onClickButtonInsert(){
        boolean textIsNotError = editTextManager.textErrorCheck(editWeight);
        if(textIsNotError){
            weight = editWeight.getText().toString();

            Cursor c = dbAdapter.selectUser();
            if(c.moveToFirst()){
                id = c.getString(USER_COL_ID_INDEX.getInt());
            }
            c.close();

            dbAdapter.insertHistory(id,weight, date, time);
            dbAdapter.updateProfileWeight(id,weight);

            textTime.setText("測定時刻：" + time);

            buttonInsert.setVisibility(View.GONE);

            editStop();
            Toast.makeText(getActivity(), "登録が完了しました", Toast.LENGTH_SHORT).show();
        }
    }

    void onClickButtonUpdate(){
        boolean editTextIsError = editTextManager.textErrorCheck(editWeight);
        if(editTextIsError){
            weight = editWeight.getText().toString();
            dbAdapter.updateHistory(weight,date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String today = sdf.format(new Date());
            if(date.equals(today)) {
                dbAdapter.updateProfileWeight(id, weight);
            }
            editStop();
            Toast.makeText(getActivity(), "更新が完了しました", Toast.LENGTH_SHORT).show();
        }
    }
}
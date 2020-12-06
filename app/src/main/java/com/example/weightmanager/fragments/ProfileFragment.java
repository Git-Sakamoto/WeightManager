package com.example.weightmanager.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.weightmanager.DBAdapter;
import com.example.weightmanager.DecimalDigitsInputFilter;
import com.example.weightmanager.EditTextManager;
import com.example.weightmanager.R;
import static com.example.weightmanager.enums.DatabaseEnum.*;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button buttonEdit,buttonCancel,buttonUpdate;
    EditText editName,editHeight,editWeight,editTargetWeight;
    String id,name,height,weight,targetWeight;
    DBAdapter dbAdapter;
    EditTextManager editTextManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextManager = new EditTextManager(getActivity());
        dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();
        Cursor c = dbAdapter.selectUser();
        if(c.moveToFirst()) {
            id = String.valueOf(c.getInt(USER_COL_ID_INDEX.getInt()));
            name = c.getString(USER_COL_NAME_INDEX.getInt());
            height = c.getString(USER_COL_HEIGHT_INDEX.getInt());
            weight = c.getString(USER_COL_WEIGHT_INDEX.getInt());
            targetWeight = c.getString(USER_COL_TARGET_WEIGHT_INDEX.getInt());
        }
        c.close();

        findViews(view);

        setText(name,height,weight,targetWeight);

        buttonEdit.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_edit://編集ボタン
                editStart();
                break;
            case R.id.button_cancel://キャンセルボタン
                editStop();
                break;
            case R.id.button_update://更新ボタン
                updateProfile();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbAdapter.closeDB();
    }

    void findViews(View view){
        editName = view.findViewById(R.id.profile_edit_name);
        editHeight = view.findViewById(R.id.profile_edit_height);
        editHeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        editWeight = view.findViewById(R.id.profile_edit_weight);
        editWeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});
        editTargetWeight = view.findViewById(R.id.profile_edit_target_weight);
        editTargetWeight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter()});

        buttonEdit = view.findViewById(R.id.button_edit);
        buttonCancel = view.findViewById(R.id.button_cancel);
        buttonUpdate = view.findViewById(R.id.button_update);
    }

    void setText(String name,String height,String weight,String targetWeight){
        editName.setText(name);
        editHeight.setText(height);
        editWeight.setText(weight);
        editTargetWeight.setText(targetWeight);
    }

    void editStart(){
        buttonEdit.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.VISIBLE);
        buttonUpdate.setVisibility(View.VISIBLE);

        EditTextManager editTextManager = new EditTextManager(getActivity());
        editTextManager.startEditing(editName,editHeight,editWeight,editTargetWeight);
    }

    void editStop(){
        buttonEdit.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.GONE);
        buttonUpdate.setVisibility(View.GONE);

        editTextManager.stopEditing(editName,editHeight,editWeight,editTargetWeight);

        setText(name,height,weight,targetWeight);
    }

    void updateProfile(){
        boolean textIsNotError = editTextManager.textErrorCheck(editName,editHeight,editWeight,editTargetWeight);
        if(textIsNotError){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("プロフィールの更新");
            builder.setMessage("プロフィールを更新しますか？");
            builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    name = editName.getText().toString();
                    height = editHeight.getText().toString();
                    weight = editWeight.getText().toString();
                    targetWeight = editTargetWeight.getText().toString();

                    dbAdapter.updateProfile(id, name, height, weight, targetWeight);
                    editStop();

                }
            });
            builder.setNegativeButton("いいえ", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
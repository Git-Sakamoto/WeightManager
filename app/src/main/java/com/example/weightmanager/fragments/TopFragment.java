package com.example.weightmanager.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weightmanager.DBAdapter;
import com.example.weightmanager.R;

public class TopFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.button_weight_input).setOnClickListener(this);
        view.findViewById(R.id.button_history).setOnClickListener(this);
        view.findViewById(R.id.button_profile).setOnClickListener(this);
        view.findViewById(R.id.button_setting).setOnClickListener(this);

        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();
        Cursor c= dbAdapter.selectUser();
        if(c.getCount() == 0){
            Navigation.findNavController(view).navigate(R.id.action_topFragment_to_userInputDialogFragment);
        }
        dbAdapter.closeDB();
        c.close();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_weight_input:
                TopFragmentDirections.ActionTopFragmentToWeightInputFragment action = TopFragmentDirections.actionTopFragmentToWeightInputFragment(null);
                Navigation.findNavController(view).navigate(action);
                break;
            case R.id.button_history:
                Navigation.findNavController(view).navigate(R.id.action_topFragment_to_historyFragment);
                break;
            case R.id.button_profile:
                Navigation.findNavController(view).navigate(R.id.action_topFragment_to_profileFragment);
                break;
            case R.id.button_setting:
                Navigation.findNavController(view).navigate(R.id.action_topFragment_to_settingFragment);
                break;
        }
    }
}

/*class MyClickAdapter implements OnClickListener (
        public void onClick(View v){
            switch(v.getId()){
                case R.id.button1:
                    処理1
                    break;
                case R.id.button2:
                    処理2
                    break;
            }
        }
}*/

/*
MyClickAdapter myClickListener = new MyClickAdapter;
Button btn1 = (Button) findViewById( R.id.button1 );
btn1.setOnClickListener( myClickListener );
*/
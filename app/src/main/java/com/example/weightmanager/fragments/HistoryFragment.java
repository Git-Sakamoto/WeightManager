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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weightmanager.DBAdapter;
import com.example.weightmanager.R;
import static com.example.weightmanager.enums.DatabaseEnum.*;

public class HistoryFragment extends Fragment {
    ListView listHistory;
    TextView textTargetWeight;
    DBAdapter dbAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textTargetWeight = view.findViewById(R.id.text_target_weight);
        listHistory = view.findViewById(R.id.list_history);

        dbAdapter = new DBAdapter(getActivity());

        listHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String date = ((TextView)view.findViewById(R.id.text_date)).getText().toString();
                HistoryFragmentDirections.ActionHistoryFragmentToWeightInputFragment action = HistoryFragmentDirections.actionHistoryFragmentToWeightInputFragment(date);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        dbAdapter.openDB();
        Cursor c = dbAdapter.selectUser();
        if(c.moveToFirst()){
            textTargetWeight.setText(c.getString(USER_COL_TARGET_WEIGHT_INDEX.getInt()));
        }
        c.close();
        listHistory.setAdapter(dbAdapter.getHistoryList(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();
        dbAdapter.closeDB();
    }
}
package com.murtaza.i180595_i180599;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhoneFragment extends Fragment {

    List<CallRecord> callList = null;
    RecyclerView recyclerViewCall;
    View view;
    CallHistoryAdapter adapter;

    public PhoneFragment() {
        if (callList == null) {
            callList = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone, container, false);

        CallRecordDBHelper helper = new CallRecordDBHelper(view.getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM CALLRECORD", new String[]{});
        if (cursor != null)
            cursor.moveToFirst();

        do {
            int value1 = R.drawable.no_dp;
            if (cursor != null) {
                value1 = cursor.getInt(1);
            }
            String value2 = null;
            if (cursor != null) {
                value2 = cursor.getString(2);
            }
            String value3 = null;
            if (cursor != null) {
                value3 = cursor.getString(3);
            }
            String value4 = null;
            if (cursor != null) {
                value4 = cursor.getString(4);
            }
            callList.add(new CallRecord(value1, value2, value3, value4));
        } while (cursor.moveToNext());

        recyclerViewCall = view.findViewById(R.id.CallList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewCall.setLayoutManager(layoutManager);
        adapter = new CallHistoryAdapter(callList, view.getContext());
        recyclerViewCall.setAdapter(adapter);

        return view;
    }

}
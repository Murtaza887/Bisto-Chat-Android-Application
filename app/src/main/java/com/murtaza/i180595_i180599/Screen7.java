package com.murtaza.i180595_i180599;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Screen7 extends AppCompatActivity {

    List<CallRecord> list = null;
    RecyclerView recyclerView;

    public Screen7() {
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_screen7);

//        recyclerView = findViewById(R.id.calllist);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen7.this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(new CallHistoryAdapter(list, Screen7.this));

//        ImageView chat = findViewById(R.id.chat_icon);
//        chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Screen7.this, Screen4.class);
//                startActivity(intent);
//            }
//        });

    }
}
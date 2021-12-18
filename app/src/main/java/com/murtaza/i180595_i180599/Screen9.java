package com.murtaza.i180595_i180599;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Screen9 extends AppCompatActivity {

    String name, time, status;
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_screen9);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Name");
            time = extras.getString("Time");
            image = extras.getInt("Image", -1);
            status = extras.getString("Status");
        }

        TextView textView = findViewById(R.id.name);
        textView.setText(name);

        CallRecordDBHelper helper = new CallRecordDBHelper(Screen9.this);
        SQLiteDatabase database = helper.getWritableDatabase();

        ImageView imageView = findViewById(R.id.profile_image);
        imageView.setImageResource(image);

        ImageView dialer = findViewById(R.id.phoneCall);
        dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                helper.insertData(image, name, currentTime, status, database);
                Intent intent = new Intent(Screen9.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
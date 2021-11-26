package com.murtaza.i180595_i180599;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Screen9 extends AppCompatActivity {

    String name;
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
            image = extras.getInt("Image", -1);
        }

        TextView textView = findViewById(R.id.name);
        textView.setText(name);

        ImageView imageView = findViewById(R.id.profile_image);
        imageView.setImageResource(image);

        ImageView dialer = findViewById(R.id.phoneCall);
        dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("callrecord");
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                reference.push().setValue(new CallRecord(image, name, currentTime));
                Intent intent = new Intent(Screen9.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
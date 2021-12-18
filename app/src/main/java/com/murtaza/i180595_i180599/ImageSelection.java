package com.murtaza.i180595_i180599;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ImageSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_image_selection);

        byte[] byteArray = getIntent().getByteArrayExtra("Image");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.7));

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton radioButton1 = findViewById(R.id.radio_button_1);
        RadioButton radioButton2 = findViewById(R.id.radio_button_2);
        RadioButton radioButton3 = findViewById(R.id.radio_button_3);
        RadioButton radioButton4 = findViewById(R.id.radio_button_4);
        RadioButton radioButton5 = findViewById(R.id.radio_button_5);

        Button btn = findViewById(R.id.send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageSelection.this, ImageMessaging.class);
                intent.putExtra("Image", byteArray);
                startActivity(intent);
            }
        });
    }
}
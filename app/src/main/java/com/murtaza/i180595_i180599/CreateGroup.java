package com.murtaza.i180595_i180599;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateGroup extends AppCompatActivity {

    EditText text1, text2, text3;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_create_group);

        text1 = findViewById(R.id.name1);
        text2 = findViewById(R.id.name2);
        text3 = findViewById(R.id.name3);
        btn = findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = text1.getText().toString();
                String second = text2.getText().toString();
                String third = text3.getText().toString();
                Intent intent = new Intent(CreateGroup.this, Home.class);
                intent.putExtra("first", first);
                intent.putExtra("second", second);
                intent.putExtra("third", third);
                startActivity(intent);
            }
        });
    }
}
package com.murtaza.i180595_i180599;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateGroup extends AppCompatActivity {
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    String first, second;
    Button btn;
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_create_group);

        checkBox1 = findViewById(R.id.option1);
        checkBox2 = findViewById(R.id.option2);
        checkBox3 = findViewById(R.id.option3);
        checkBox4 = findViewById(R.id.option4);
        checkBox5 = findViewById(R.id.option5);
        btn = findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked()) {
                    list.add(checkBox1.getText().toString());
                }
                if (checkBox2.isChecked()) {
                    list.add(checkBox2.getText().toString());
                }
                if (checkBox3.isChecked()) {
                    list.add(checkBox3.getText().toString());
                }
                if (checkBox4.isChecked()) {
                    list.add(checkBox4.getText().toString());
                }
                if (checkBox5.isChecked()) {
                    list.add(checkBox5.getText().toString());
                }
                first = list.get(0);
                second = list.get(1);
                Intent intent = new Intent(CreateGroup.this, Home.class);
                intent.putExtra("first", first);
                intent.putExtra("second", second);
                startActivity(intent);
            }
        });
    }
}
package com.murtaza.i180595_i180599;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Screen2 extends AppCompatActivity {
    EditText field1, field2;
    Boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_screen2);

        DBHelper helper = new DBHelper(Screen2.this);
        SQLiteDatabase database = helper.getReadableDatabase();

        TextView regNow = findViewById(R.id.register);
        regNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Screen2.this, Screen3.class);
                startActivity(intent);
            }
        });

        Button button = findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                field1 = findViewById(R.id.email);
                field2 = findViewById(R.id.password);
                String email = field1.getText().toString();
                String password = field2.getText().toString();

                if (!email.equals("") && !password.equals("")) {
                    Cursor cursor = database.rawQuery("SELECT email, password FROM USERS", new String[]{});

                    if (cursor != null)
                        cursor.moveToFirst();

                    do {
                        String value1 = null;
                        if (cursor != null) {
                            value1 = cursor.getString(0);
                        }
                        String value2 = null;
                        if (cursor != null) {
                            value2 = cursor.getString(1);
                        }

                        if (email.equals(value1) && password.equals(value2)) {
                            loggedIn = true;
                            CurrentUser currentUser = new CurrentUser();
                            currentUser.setUser(email);
                            Intent intent = new Intent(Screen2.this, Home.class);
                            startActivity(intent);
                        }
                    } while (cursor.moveToNext());

                    if (!loggedIn) {
                        Toast.makeText(Screen2.this, "Incorrect Email or Password! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Screen2.this, "Please fill out all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
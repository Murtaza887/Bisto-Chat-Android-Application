package com.murtaza.i180595_i180599;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class SendNotifications extends AppCompatActivity {

    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    token = task.getResult();
                    Toast.makeText(SendNotifications.this, token, Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditText title = findViewById(R.id.title);
        EditText message = findViewById(R.id.message);
        EditText userToken = findViewById(R.id.usertoken);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!title.getText().toString().isEmpty() && !message.getText().toString().isEmpty()) {
                    FcmNotificationsSender sender = new FcmNotificationsSender(token, title.getText().toString(), message.getText().toString(), getApplicationContext(),SendNotifications.this);
                    sender.SendNotifications();
                }
                else {
                    Toast.makeText(SendNotifications.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
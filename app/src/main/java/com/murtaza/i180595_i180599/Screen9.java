package com.murtaza.i180595_i180599;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class Screen9 extends AppCompatActivity {

    String name, time, status;
    int image;
    private String appId = "6092e8edb7c24980b2c184cfd492e8fe";
    private String channelName = "Bisto";
    private String token = "0066092e8edb7c24980b2c184cfd492e8feIABwsA8Yw8kYXmjIDfneWwwV6E4oz9MgdHdN16aJGJqHe49S1mIAAAAAEACVNqlcAxrKYQEAAQABGsph";
    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

    };
    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_screen9);

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        CurrentUser currentUser = new CurrentUser();
        String user = "";
        if (currentUser.getUser().equals("murtazahassnain17@gmail.com")) {
            user = "Murtaza";
        }
        if (currentUser.getUser().equals("saifullah@gmail.com")) {
            user = "Saifullah";
        }
        FcmNotificationsSender sender = new FcmNotificationsSender("cik7iyk3QaaMh2qwWEDhOI:APA91bES7StBog6--aPrU-Snzv3hFaKncR0t29lXrQLpmJGXr9hiVREXagO_VVA7o3soe0a6G-04fVfk8HGSW7qefXC715tZCiGG4CmraQ02QM_OLaDvtZ6hy_f8zobEBkPcVC3igUWA", user, "Calling...", getApplicationContext(),Screen9.this);
        sender.SendNotifications();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Name");
            time = extras.getString("Time");
            image = extras.getInt("Image", -1);
            status = extras.getString("Status");
        }

        TextView textView = findViewById(R.id.name);
        if (name != null)
            textView.setText(name);
        else
            textView.setText("Murtaza");

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

    protected void onDestroy() {
        super.onDestroy();
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    private void initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error");
        }
        mRtcEngine.joinChannel(token, channelName, "", 0);
    }
}
package com.murtaza.i180595_i180599;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import io.agora.rtc.video.VideoCanvas;

public class VideoCall extends AppCompatActivity {

    String name, time, status;
    int image;

    private String appId = "6092e8edb7c24980b2c184cfd492e8fe";
    private String channelName = "Bisto";
    private String token = "0066092e8edb7c24980b2c184cfd492e8feIADSGoC0bg2Jx/mVGHOTz9ZslY2jXnvqdNNfdT7nCReKAI9S1mIAAAAAEACVNqlciBvKYQEAAQCGG8ph";
    private RtcEngine mRtcEngine;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the remote user joining the channel to get the uid of the user.
        public void onUserJoined(int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                    setupRemoteVideo(uid);
                }
            });
        }
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
        setContentView(R.layout.activity_video_call);

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
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
        FcmNotificationsSender sender = new FcmNotificationsSender("cik7iyk3QaaMh2qwWEDhOI:APA91bES7StBog6--aPrU-Snzv3hFaKncR0t29lXrQLpmJGXr9hiVREXagO_VVA7o3soe0a6G-04fVfk8HGSW7qefXC715tZCiGG4CmraQ02QM_OLaDvtZ6hy_f8zobEBkPcVC3igUWA", user, "Calling...", getApplicationContext(),VideoCall.this);
        sender.SendNotifications();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Name");
            time = extras.getString("Time");
            image = extras.getInt("Image", -1);
            status = extras.getString("Status");
        }

        CallRecordDBHelper helper = new CallRecordDBHelper(VideoCall.this);
        SQLiteDatabase database = helper.getWritableDatabase();

        ImageView dialer = findViewById(R.id.phoneCall);
        dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                helper.insertData(image, name, currentTime, status, database);
                Intent intent = new Intent(VideoCall.this, Home.class);
                startActivity(intent);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();

        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    // Java
    private void initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine.enableVideo();

        FrameLayout container = findViewById(R.id.local_video_view_container);
        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));

        // Join the channel with a token.
        mRtcEngine.joinChannel(token, channelName, "", 0);
    }

    // Java
    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
    }
}
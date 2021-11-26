package com.murtaza.i180595_i180599;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Screen5 extends ScreenshotDetectionActivity {

    List<Message> list = null;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    String name, last_active, phoneNumber;
    int image;
    int index;
    Bitmap bitmap;

    public Screen5() {
        if (list == null)
            list = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_screen5);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            index = extras.getInt("Index", -1);
            name = extras.getString("Name");
            last_active = extras.getString("Last Active");
            image = extras.getInt("Image", -1);
            phoneNumber = extras.getString("Phone Number");
        }

        TextView textView = findViewById(R.id.contactName);
        textView.setText(name);

        TextView textView1 = findViewById(R.id.onlineStatus);
        if (!last_active.equals("Mon")) {
            textView1.setText("Online");
        }
        else {
            textView1.setText(last_active);
        }

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Screen5.this, Home.class);
                startActivity(intent);
            }
        });

        MessagesDBHelper helper = new MessagesDBHelper(Screen5.this);
        SQLiteDatabase database = helper.getReadableDatabase();

        String query = "SELECT * FROM MESSAGES where username = '" + name + "'";
        Cursor cursor = database.rawQuery(query, new String[]{});
        if (cursor != null)
            cursor.moveToFirst();
        do {
            String message = null;
            if (cursor != null) {
                message = cursor.getString(1);
            }
            String time = null;
            if (cursor != null) {
                time = cursor.getString(2);
            }
            String username = null;
            if (cursor != null) {
                username = cursor.getString(3);
            }
            String receiver = null;
            if (cursor != null) {
                receiver = cursor.getString(4);
            }
            String sender = null;
            if (cursor != null) {
                sender = cursor.getString(5);
            }
            list.add(new Message(message, time, username, receiver, sender));
        } while (cursor.moveToNext());

        recyclerView = findViewById(R.id.messages);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(list, Screen5.this);
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.searchbar);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

        });

//        ImageView img = findViewById(R.id.phone_logo);
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = getApplicationContext();
//
//                FirebaseMessaging.getInstance().subscribeToTopic("all");
//                FcmNotificationsSender sender = new FcmNotificationsSender("doHfNOduRG283Nx_DWlrec:APA91bF5uEtpkAdt2URl6jEZBHr81E6GCLUs_J2f0ZAnbdGrmJcruwHW7zIYkHPn9FFt70yMfkwBRCGJRKMsRReZjmZt1dhRAa9kXVrlFnS-KAFhl69U7wM2MRIub52hgG6ow08XwsTc", currentUser.getUser(), "Calling...", getApplicationContext(), Screen5.this);
//                sender.SendNotifications();
//                Intent intent = new Intent(Screen5.this, Screen9.class);
//                intent.putExtra("Name", name);
//                intent.putExtra("Image", image);
//                startActivity(intent);
//            }
//        });

        ImageView send = findViewById(R.id.sendMessage);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.messageText);
                String text = editText.getText().toString();
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                MessagesDBHelper helper = new MessagesDBHelper(Screen5.this);
                SQLiteDatabase database = helper.getWritableDatabase();

                CurrentUser currentUser = new CurrentUser();
                helper.insertData(text, currentTime, name, name, currentUser.getUser(), database);

                recyclerView = findViewById(R.id.messages);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new ChatAdapter(list, Screen5.this);
                recyclerView.setAdapter(adapter);

                editText.getText().clear();
            }
        });

        ImageView imageView = findViewById(R.id.camera_upload_option);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(Screen5.this);
            }
        });
    }

    private void filter(String text) {
        List<Message> filteredList = new ArrayList<>();
        for (Message message: list) {
            if (message.getMessage().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(message);
            }
        }
        adapter.filterListChat(filteredList);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        bitmap = selectedImage;
                        ImageView imageView = findViewById(R.id.image);
                        imageView.setImageBitmap(bitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                cursor.close();
                            }
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ImageView imageView = findViewById(R.id.image);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onScreenCaptured(String path) {
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FcmNotificationsSender sender = new FcmNotificationsSender("doHfNOduRG283Nx_DWlrec:APA91bF5uEtpkAdt2URl6jEZBHr81E6GCLUs_J2f0ZAnbdGrmJcruwHW7zIYkHPn9FFt70yMfkwBRCGJRKMsRReZjmZt1dhRAa9kXVrlFnS-KAFhl69U7wM2MRIub52hgG6ow08XwsTc", "Screenshot Captured", path, getApplicationContext(),Screen5.this);
        sender.SendNotifications();
    }

    @Override
    public void onScreenCapturedWithDeniedPermission() {
        Toast.makeText(this, "Please grant read external storage permission for screenshot detection", Toast.LENGTH_SHORT).show();
    }
}
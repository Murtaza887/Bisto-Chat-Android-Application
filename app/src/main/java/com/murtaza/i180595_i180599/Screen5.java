package com.murtaza.i180595_i180599;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Screen5 extends ScreenshotDetectionActivity {

    List<Message> list = null;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    GroupMessageAdapter groupAdapter;
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
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        if (last_active.equals(currentTime)) {
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

        String url = "http://192.168.18.152/read2.php";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    for (int k = 0; k < object.getJSONArray("messages").length(); k++) {
                        JSONObject toAdd = (JSONObject) object.getJSONArray("messages").get(k);
                        Message message = new Message(toAdd.getInt("id"), toAdd.getString("message"), toAdd.getString("time"), toAdd.getString("username"), toAdd.getString("to"), toAdd.getString("from"));
                        if (message.getUsername().equals(name)) {
                            list.add(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                recyclerView = findViewById(R.id.messages);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                recyclerView.setLayoutManager(layoutManager);
                if (!name.contains(",")) {
                    adapter = new ChatAdapter(list, Screen5.this);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    groupAdapter = new GroupMessageAdapter(list, Screen5.this);
                    recyclerView.setAdapter(groupAdapter);
                }
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(Screen5.this, "Using offline feature", Toast.LENGTH_SHORT).show();
                MessagesDBHelper helper = new MessagesDBHelper(Screen5.this);
                SQLiteDatabase database = helper.getReadableDatabase();

                Cursor cursor = database.rawQuery("SELECT * FROM MESSAGES WHERE username = '" + name + "'", new String[]{});
                if (cursor != null)
                    cursor.moveToFirst();

                do {
                    String value1 = null;
                    if (cursor != null) {
                        value1 = cursor.getString(1);
                    }
                    String value2 = null;
                    if (cursor != null) {
                        value2 = cursor.getString(2);
                    }
                    String value3 = null;
                    if (cursor != null) {
                        value3 = cursor.getString(3);
                    }
                    String value4 = null;
                    if (cursor != null) {
                        value4 = cursor.getString(4);
                    }
                    String value5 = null;
                    if (cursor != null) {
                        value5 = cursor.getString(5);
                    }
                    list.add(new Message(100, value1, value2, value3, value4, value5));
                } while (cursor.moveToNext());

                recyclerView = findViewById(R.id.messages);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                recyclerView.setLayoutManager(layoutManager);
                if (!name.contains(",")) {
                    adapter = new ChatAdapter(list, Screen5.this);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    groupAdapter = new GroupMessageAdapter(list, Screen5.this);
                    recyclerView.setAdapter(groupAdapter);
                }
            }
        });

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

        ImageView img = findViewById(R.id.phone_logo);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Screen5.this, Screen9.class);
                intent.putExtra("Name", name);
                intent.putExtra("Image", image);
                intent.putExtra("Status", "outgoing");
                startActivity(intent);
            }
        });

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

                String url = "http://192.168.18.152/insert2.php";
                RequestParams requestParams = new RequestParams();
                requestParams.put("message", text);
                requestParams.put("time", currentTime);
                requestParams.put("username", name);
                requestParams.put("to", name);
                requestParams.put("from", currentUser.getUser());

                new AsyncHttpClient().post(Screen5.this, url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        try {
                            JSONObject json = new JSONObject(new String(bytes));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        list = new ArrayList<>();
                        String url = "http://192.168.18.152/read2.php";
                        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                                try {
                                    JSONObject object = new JSONObject(new String(bytes));
                                    for (int k = 0; k < object.getJSONArray("messages").length(); k++) {
                                        JSONObject toAdd = (JSONObject) object.getJSONArray("messages").get(k);
                                        Message message = new Message(toAdd.getInt("id"), toAdd.getString("message"), toAdd.getString("time"), toAdd.getString("username"), toAdd.getString("to"), toAdd.getString("from"));
                                        if (message.getUsername().equals(name)) {
                                            list.add(message);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                recyclerView = findViewById(R.id.messages);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                                recyclerView.setLayoutManager(layoutManager);
                                if (!name.contains(",")) {
                                    adapter = new ChatAdapter(list, Screen5.this);
                                    recyclerView.setAdapter(adapter);
                                }
                                else {
                                    groupAdapter = new GroupMessageAdapter(list, Screen5.this);
                                    recyclerView.setAdapter(groupAdapter);
                                }
                            }

                            @Override
                            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                                Toast.makeText(Screen5.this, "Using Offline Feature", Toast.LENGTH_SHORT).show();
                                MessagesDBHelper helper2 = new MessagesDBHelper(Screen5.this);
                                SQLiteDatabase database2 = helper2.getReadableDatabase();

                                Cursor cursor2 = database2.rawQuery("SELECT * FROM MESSAGES WHERE username = name", new String[]{});
                                if (cursor2 != null)
                                    cursor2.moveToFirst();

                                do {
                                    String value1 = null;
                                    if (cursor2 != null) {
                                        value1 = cursor2.getString(1);
                                    }
                                    String value2 = null;
                                    if (cursor2 != null) {
                                        value2 = cursor2.getString(2);
                                    }
                                    String value3 = null;
                                    if (cursor2 != null) {
                                        value3 = cursor2.getString(3);
                                    }
                                    String value4 = null;
                                    if (cursor2 != null) {
                                        value4 = cursor2.getString(4);
                                    }
                                    String value5 = null;
                                    if (cursor2 != null) {
                                        value5 = cursor2.getString(5);
                                    }
                                    list.add(new Message(100, value1, value2, value3, value4, value5));
                                } while (cursor2.moveToNext());

                                recyclerView = findViewById(R.id.messages);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                                recyclerView.setLayoutManager(layoutManager);
                                if (!name.contains(",")) {
                                    adapter = new ChatAdapter(list, Screen5.this);
                                    recyclerView.setAdapter(adapter);
                                }
                                else {
                                    groupAdapter = new GroupMessageAdapter(list, Screen5.this);
                                    recyclerView.setAdapter(groupAdapter);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(Screen5.this, "Using Offline Feature", Toast.LENGTH_SHORT).show();
                        MessagesDBHelper helper1 = new MessagesDBHelper(Screen5.this);
                        SQLiteDatabase database1 = helper1.getWritableDatabase();
                        Boolean result = helper1.insertData(text, currentTime, name, name, currentUser.getUser(), database1);
                        if (result.equals(true)) {
                            recyclerView = findViewById(R.id.messages);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Screen5.this);
                            recyclerView.setLayoutManager(layoutManager);
                            if (!name.contains(",")) {
                                adapter = new ChatAdapter(list, Screen5.this);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                groupAdapter = new GroupMessageAdapter(list, Screen5.this);
                                recyclerView.setAdapter(groupAdapter);
                            }
                        }
                    }
                });

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
        FcmNotificationsSender sender = new FcmNotificationsSender("/topics/all", "Screenshot Captured", path, getApplicationContext(),Screen5.this);
        sender.SendNotifications();
    }

    @Override
    public void onScreenCapturedWithDeniedPermission() {
        Toast.makeText(this, "Please grant read external storage permission for screenshot detection", Toast.LENGTH_SHORT).show();
    }
}
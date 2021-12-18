package com.murtaza.i180595_i180599;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class EditMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_edit_message);

        EditText editText = findViewById(R.id.edit);
        Button button = findViewById(R.id.submit);

        int id = getIntent().getIntExtra("Id", 0);
        String text = getIntent().getStringExtra("Text");
        String time = getIntent().getStringExtra("Time");
        String username = getIntent().getStringExtra("Username");
        String to = getIntent().getStringExtra("To");
        String from = getIntent().getStringExtra("From");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://192.168.18.152/update2.php";
                RequestParams requestParams = new RequestParams();
                requestParams.put("id", id);
                requestParams.put("message", editText.getText().toString());
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                requestParams.put("time", currentTime);
                requestParams.put("username", username);
                requestParams.put("to", to);
                requestParams.put("from", from);

                new AsyncHttpClient().post(EditMessage.this, url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Toast.makeText(EditMessage.this, "Message edited!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditMessage.this, Home.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(EditMessage.this, "Message editing failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
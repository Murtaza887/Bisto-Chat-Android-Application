package com.murtaza.i180595_i180599;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class DeleteMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_delete_message);

        int id = getIntent().getIntExtra("Id", 0);
        String url = "http://192.168.18.152/delete2.php";
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", id);
        new AsyncHttpClient().post(DeleteMessage.this, url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(DeleteMessage.this, "Message Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeleteMessage.this, Home.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

    }
}
package com.murtaza.i180595_i180599;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Home extends AppCompatActivity {

    public static Context contextOfApplication;
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_home);

        contextOfApplication = getApplicationContext();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navbar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {

                case R.id.chat:
                    selectedFragment = new ChatFragment();
                    break;

                case R.id.camera:
                    selectedFragment = new CameraFragment();
                    break;

                case R.id.phone:
                    selectedFragment = new PhoneFragment();
                    break;

                case R.id.contacts:
                    selectedFragment = new ContactFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    Contact getContactIntent() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        Contact contact = new Contact();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String contact1 = extras.getString("first");
            String contact2 = extras.getString("second");
            String groupName = contact1 + "," + contact2;
            String groupPhone = "-";
            contact = new Contact(R.drawable.no_dp, currentTime, "This is a Sample message", groupName, groupPhone);
            ChatDBHelper helper = new ChatDBHelper(Home.this);
            SQLiteDatabase database = helper.getWritableDatabase();
            helper.insertData(R.drawable.no_dp, currentTime, "This is a Sample message", groupName, groupPhone, database);
        }
        return contact;
    }
}
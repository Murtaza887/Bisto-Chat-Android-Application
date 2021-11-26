package com.murtaza.i180595_i180599;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessagesDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Message.db";
    public static final int DB_VER = 1;
    Context context;

    public MessagesDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE MESSAGES (_id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, time TEXT, username TEXT, receiver TEXT, sender TEXT)";
        sqLiteDatabase.execSQL(create);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        insertData("This is a sample message", currentTime, "AaSaif", "saifullah@gmail.com", "murtazahassnain17@gmail.com", sqLiteDatabase);
        insertData("This is a sample message", currentTime, "Aamir Bhai", "Aamir Bhai", "murtazahassnain17@gmail.com", sqLiteDatabase);
        insertData("This is a sample message", currentTime, "Abbu", "Abbu", "murtazahassnain17@gmail.com", sqLiteDatabase);
        insertData("This is a sample message", currentTime, "Abdullah", "Abdullah", "murtazahassnain17@gmail.com", sqLiteDatabase);
        insertData("This is a sample message", currentTime, "Abdullah Akhtar", "Abdullah Akhtar", "murtazahassnain17@gmail.com", sqLiteDatabase);
    }

    public Boolean insertData(String message, String time, String username, String receiver, String sender, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("message", message);
        values.put("time", time);
        values.put("receiver", receiver);
        values.put("sender", sender);

        double result = database.insert("MESSAGES", null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.murtaza.i180595_i180599;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallRecordDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CallRecord.db";
    public static final int DB_VER = 1;
    Context context;

    public CallRecordDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE CALLRECORD (_id INTEGER PRIMARY KEY AUTOINCREMENT, image INTEGER, name TEXT, time TEXT, status TEXT)";
        sqLiteDatabase.execSQL(create);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        insertData(R.drawable.prof_pic5, "Abdullah Akhtar", currentTime, "incoming", sqLiteDatabase);
    }

    public Boolean insertData(int image, String name, String time, String status, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("image", image);
        values.put("name", name);
        values.put("time", time);
        values.put("status", status);

        double result = database.insert("CALLRECORD", null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

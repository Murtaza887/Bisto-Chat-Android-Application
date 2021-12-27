package com.murtaza.i180595_i180599;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ChatFragment extends Fragment {

    List<Contact> chatList = null;
    int pictures[] = {R.drawable.prof_pic1, R.drawable.prof_pic2, R.drawable.prof_pic3, R.drawable.prof_pic4, R.drawable.prof_pic5};
    RecyclerView recyclerViewChat;
    RvAdapter adapter;
    ChatDBHelper helper;
    SQLiteDatabase database;
    int counter = 0;
    View view;

    public ChatFragment() {
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        helper = new ChatDBHelper(view.getContext());
        database = helper.getWritableDatabase();

        EditText editText = view.findViewById(R.id.searchbar);
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

        recyclerViewChat = view.findViewById(R.id.Chat_list);

        ChatDBHelper helper = new ChatDBHelper(view.getContext());
        SQLiteDatabase database = helper.getReadableDatabase();

        String query = "SELECT * FROM CONTACTS";
        Cursor cursor = database.rawQuery(query, new String[]{});
        if (cursor != null)
            cursor.moveToFirst();
        do {
            int image = -1;
            if (cursor != null) {
                image = cursor.getInt(3);
            }
            String last_active = null;
            if (cursor != null) {
                last_active = cursor.getString(5);
            }
            String last_message = null;
            if (cursor != null) {
                last_message = cursor.getString(4);
            }
            String name = null;
            if (cursor != null) {
                name = cursor.getString(1);
            }
            String phone = null;
            if (cursor != null) {
                phone = cursor.getString(2);
            }
            chatList.add(new Contact(image, last_active, last_message, name, phone));
        } while (cursor.moveToNext());

        checkPermission();

        ImageView imageView = view.findViewById(R.id.back_arrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Screen2.class);
                startActivity(intent);
            }
        });

        ImageView imageView2 = view.findViewById(R.id.searchicon);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText x = editText.findViewById(R.id.searchbar);
                x.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void filter(String text) {
        ArrayList<Contact> filteredList = new ArrayList<>();
        List<Contact> temp = new ArrayList<>();
        for (int i = 0; i < chatList.size()-1; i++) {
            temp.add(chatList.get(i));
        }
        for (Contact contact: temp) {
            if (contact.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(contact);
            }
        }
        adapter.filterList(filteredList);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.READ_CONTACTS}, 100);
        }
        else {
            getContactList();
        }
    }

    private void getContactList() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Context applicationContext = Home.getContextOfApplication();
        Cursor cursor = applicationContext.getContentResolver().query(uri, null , null, null , sort);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (counter == 5) {
                    break;
                }
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                Cursor phoneCursor = applicationContext.getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contact model;
                    if (number.equals("+92 334 5820814") || number.equals("+92 310 5376009")) {
                        model = new Contact(pictures[counter], currentTime, "This is a sample message.", name, number);
                    }
                    else {
                        String days[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                        int random = new Random().nextInt(7);
                        model = new Contact(pictures[counter], days[random], "This is a sample message.", name, number);
                    }
                    chatList.add(model);
                    phoneCursor.close();
                }
                counter++;
            }
            cursor.close();
        }
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Contact contact = ((Home) getActivity()).getContactIntent();
        if (contact != null) {
            chatList.add(contact);
        }
        adapter = new RvAdapter(chatList, view.getContext());
        recyclerViewChat.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        }
        else {
            Toast.makeText(view.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
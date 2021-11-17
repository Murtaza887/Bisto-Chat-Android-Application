package com.murtaza.i180595_i180599;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    List<Contact> chatList = null;
    int pictures[] = {R.drawable.prof_pic1, R.drawable.prof_pic2, R.drawable.prof_pic3, R.drawable.no_dp, R.drawable.no_dp};
    RecyclerView recyclerViewChat;
    RvAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("contacts");
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
        for (Contact contact: chatList) {
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
                    if (number.equals("+92 334 5820814") || number.equals("+92 310 5376009")) {
                        Contact model = new Contact(pictures[counter], currentTime, "This is a sample message.", name, number);
                        chatList.add(model);
                    }
                    else {
                        Contact model = new Contact(pictures[counter], "Mon", "This is a sample message.", name, number);
                        chatList.add(model);
                    }
                    phoneCursor.close();
                }
                counter++;
            }
            cursor.close();
        }
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
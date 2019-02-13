package com.example.kimjihyeon.myapplication.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kimjihyeon.myapplication.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String uid = getIntent().getStringExtra("uid");
        String [] uids = getIntent().getStringArrayExtra("uids");

    }

}

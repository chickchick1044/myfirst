package com.example.kimjihyeon.myapplication.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kimjihyeon.myapplication.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //1대1채팅
        String uid = getIntent().getStringExtra("uid");
        //1대다 채팅
        String [] uids = getIntent().getStringArrayExtra("uids");

    }

}

package com.example.frenbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoticeGroup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticegroup);

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView warning = findViewById(R.id.warning);
//        warning.setVisibility(View.GONE);
    }
}

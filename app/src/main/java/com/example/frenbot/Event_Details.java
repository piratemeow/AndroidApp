package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Event_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        String title=getIntent().getStringExtra("title");
        String place=getIntent().getStringExtra("place");
        String time=getIntent().getStringExtra("time");

        TextView tle=findViewById(R.id.title);
        TextView tm=findViewById(R.id.time);
        TextView location=findViewById(R.id.location);
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tle.setText(title);
        tm.setText(time);
        location.setText(place);

    }
}
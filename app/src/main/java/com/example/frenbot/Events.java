package com.example.frenbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Events extends AppCompatActivity {
    Button create_event;
    ArrayList<eventmodel> eventmodels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        RecyclerView recyclerView=findViewById(R.id.reventview);

        setupeventmodels();

        eventRVadapter rVadapter =new eventRVadapter(this,eventmodels);
        recyclerView.setAdapter(rVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        create_event = findViewById(R.id.create_event);

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Events.this,Create_event.class);
                startActivity(intent);
            }
        });
    }
    private void setupeventmodels(){
        String[] event_title= getResources().getStringArray(R.array.event_name);
        String[] event_place= getResources().getStringArray(R.array.Venue);
        String[] event_time= getResources().getStringArray(R.array.Duration);

        for(int i=0;i<event_title.length;i++){
            eventmodels.add(new eventmodel(event_title[i],event_time[i],event_place[i]));
        }
    }
}
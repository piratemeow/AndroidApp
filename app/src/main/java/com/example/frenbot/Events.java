package com.example.frenbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Events extends AppCompatActivity implements RCViewInterface {
    FloatingActionButton create_event;
    ArrayList<eventmodel> eventmodels=new ArrayList<>();

    ArrayList<Map<String,Object>> event_data = new ArrayList<>();
    eventRVadapter rVadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);
        RecyclerView recyclerView=findViewById(R.id.reventview);

        getFirestoreEvents();
        System.out.println(this.event_data.size());



        rVadapter =new eventRVadapter(this,eventmodels,this);
        recyclerView.setAdapter(rVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        create_event = findViewById(R.id.create_event);
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Events.this,Create_event.class);
                startActivity(intent);
            }
        });

    }

    private void getFirestoreEvents(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //CollectionReference eventRef = db.collection("Events");
        db.collection("Events")

                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                event_data.add(document.getData());
                            }

                            setupeventmodels();

                        } else {
                            System.out.println("Error getting document\n");
                        }

                    }
                });

    }

    private void setupeventmodels(){

        for (Map<String,Object> map: this.event_data){

            String title = map.get("title").toString();
            String location = map.get("location").toString();
            String date = map.get("date").toString();
            eventmodels.add(new eventmodel(title,date,location));
        }
        rVadapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent =new Intent(Events.this,Event_Details.class);

        intent.putExtra("title",eventmodels.get(position).getTitle());
        intent.putExtra("place",eventmodels.get(position).getPlace());
        intent.putExtra("time",eventmodels.get(position).getTime());
        startActivity(intent);
    }
}



package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Event_Details extends AppCompatActivity {
    Button interBut, goBut;
    TextView interText, goText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        String title=getIntent().getStringExtra("title");
        String place=getIntent().getStringExtra("place");
        String time=getIntent().getStringExtra("time");
        String uuid=getIntent().getStringExtra("uuid");

        interBut = findViewById(R.id.interBut);
        goBut = findViewById(R.id.goBut);
        interText = findViewById(R.id.interText);
        goText = findViewById(R.id.goText);
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


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String userId = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocument = db.collection("Users").document(userId);
        CollectionReference eventCol = db.collection("Events");
        DocumentReference eventDoc = eventCol.document(uuid);


        interBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
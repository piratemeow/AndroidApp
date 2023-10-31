package com.example.frenbot;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Create_event extends AppCompatActivity {
    TextInputEditText event_name, desc,note,location;
    Button date, create;
    private int year, month,day;
    private int event_year=0, event_month=0, event_day=0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);

        event_name = findViewById(R.id.EventName);
        desc = findViewById(R.id.Description);
        note = findViewById(R.id.Note);
        date = findViewById(R.id.Date);
        location = findViewById(R.id.location);
        create = findViewById(R.id.Create_event);

        db = FirebaseFirestore.getInstance();



        Calendar calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {

            String event_nam, event_des , event_loc , event_note;
            @Override
            public void onClick(View v) {
                event_nam = event_name.getText().toString();
                event_des = desc.getText().toString();
                event_loc = location.getText().toString();
                event_note = note.getText().toString();

                Map<String, Object> events = new HashMap<>();
                events.put("title",event_nam);
                events.put("description",event_des);
                events.put("location",event_loc);
                events.put("note",event_note);
                events.put("date",event_day+"-"+event_month+"-"+event_year);
                events.put("creator",user.toString());


                if (!event_nam.isEmpty() && !event_des.isEmpty()
                && !event_loc.isEmpty() && event_month!=0){


                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE,event_nam);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,event_des);
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION,event_loc);
                    intent.putExtra(CalendarContract.Events.ALL_DAY,true);


                    if (intent.resolveActivity(getPackageManager())!=null){
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Create_event.this, "No calender app", Toast.LENGTH_SHORT).show();
                    }

                    db.collection("Events")
                            .add(events)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Create_event.this, "Successfully created the event", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Create_event.this, "Failed to create the event", Toast.LENGTH_SHORT).show();
                                }
                            });



                }
                else {
                    Toast.makeText(Create_event.this, "Please fill the necessary fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                event_year = year;
                event_month = month;
                event_day = dayOfMonth;
            }
        },year,month,day);
        dialog.show();
    }
}
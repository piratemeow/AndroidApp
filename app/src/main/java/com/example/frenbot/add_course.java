package com.example.frenbot;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class add_course extends AppCompatActivity {
    TextInputEditText courseName, courseID, instructor, desc;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseName = findViewById(R.id.course);
        courseID = findViewById(R.id.course_id);
        desc = findViewById(R.id.Description);
        instructor = findViewById(R.id.instructor);

        add = findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String title, id, description, instruct;
                title = String.valueOf(courseName.getText());
                id = String.valueOf(courseID.getText());
                description = String.valueOf(desc.getText());
                instruct = String.valueOf(instructor.getText());

                if(TextUtils.isEmpty(title)) {
                    Toast.makeText(add_course.this,"Enter course title",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(id)) {
                    Toast.makeText(add_course.this,"Enter course id",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(instruct)) {
                    Toast.makeText(add_course.this,"Enter course instructor",Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference courseCollection = db.collection("Course");

                    DocumentReference courseDocument = courseCollection.document(userId);

                    Map<String, Object> course = new HashMap<>();
                    course.put("title",title);
                    course.put("description",description);
                    course.put("instructor",instruct);
                    course.put("id",id);

                    courseDocument.set(course)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // User data has been successfully added.
                                    Toast.makeText(add_course.this, "course added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the error.
                                    Toast.makeText(add_course.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });

                }
            }
        });


    }

}
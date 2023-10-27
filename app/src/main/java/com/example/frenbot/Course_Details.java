package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Course_Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        String title=getIntent().getStringExtra("title");
        String id1=getIntent().getStringExtra("id");
        String ins=getIntent().getStringExtra("instructor");

        TextView tle=findViewById(R.id.ctitle);
        TextView id=findViewById(R.id.course_id);
        TextView instructor=findViewById(R.id.instructor);
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tle.setText(title);
        id.setText(id1);
        instructor.setText(ins);
    }
}
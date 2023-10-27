package com.example.frenbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Academia extends AppCompatActivity implements RCViewInterface {
    FloatingActionButton add_course;
    ArrayList<coursemodel> coursemodels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academia);
        RecyclerView recyclerView=findViewById(R.id.rcview);

        setupeventmodels();

        courseRVadapter rVadapter =new courseRVadapter(this,coursemodels,this);
        recyclerView.setAdapter(rVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        add_course = findViewById(R.id.add_course);
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Academia.this,add_course.class);
                startActivity(intent);
            }
        });
    }
    private void setupeventmodels(){
        String[] course= getResources().getStringArray(R.array.course);
        String[] id= getResources().getStringArray(R.array.id);
        String[] instructor= getResources().getStringArray(R.array.instructor);

        for(int i=0;i<course.length;i++){
            coursemodels.add(new coursemodel(course[i],id[i],instructor[i]));
        }
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent =new Intent(Academia.this,Course_Details.class);
        intent.putExtra("title",coursemodels.get(position).getcourse());
        intent.putExtra("id",coursemodels.get(position).getid());
        intent.putExtra("instructor",coursemodels.get(position).getinstructor());

        startActivity(intent);

    }
}
package com.example.frenbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Login extends AppCompatActivity {

    Button signup,signin;
    FloatingActionButton fb,google,linkedin;
    LinearLayout ll;
    float v=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.signup);
        fb = findViewById(R.id.fb);
        google = findViewById(R.id.google);
        linkedin = findViewById(R.id.linkedin);
        ll=findViewById(R.id.ll);
        signin = findViewById(R.id.signin);
        fb.setTranslationY(300);
        google.setTranslationY(300);
        linkedin.setTranslationY(300);
        ll.setTranslationX(300);
        fb.setAlpha(v);
        google.setAlpha(v);
        linkedin.setAlpha(v);
        ll.setAlpha(v);
        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        linkedin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        ll.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(400).start();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
               startActivity(intent);
               finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this,"Logging in using facebook...",Toast.LENGTH_SHORT).show();
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this,"Logging in using google...",Toast.LENGTH_SHORT).show();
            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login.this,"Logging in using linkedin...",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
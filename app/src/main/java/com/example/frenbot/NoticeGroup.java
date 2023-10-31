package com.example.frenbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NoticeGroup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticegroup);

        LinearLayout ll2 = findViewById(R.id.ll2);
        TextView warning = findViewById(R.id.warning);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView name = findViewById(R.id.name);

        String groupName = getIntent().getStringExtra("groupName");
        String adminId = getIntent().getStringExtra("adminId");

        name.setText(groupName);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            if(userId.equals(adminId)) {
                warning.setVisibility(View.GONE);
            } else {
                ll2.setVisibility(View.GONE);
            }
        }

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

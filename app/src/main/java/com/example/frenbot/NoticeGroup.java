package com.example.frenbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class NoticeGroup extends AppCompatActivity implements RCViewInterface{

    public static String staticGroup;
    ArrayList<MessageItem> messageItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticegroup);


        LinearLayout ll2 = findViewById(R.id.ll2);
        TextView warning = findViewById(R.id.warning);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView name = findViewById(R.id.name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextInputEditText message = findViewById((R.id.message));

        String groupName = getIntent().getStringExtra("groupName");
        String adminId = getIntent().getStringExtra("adminId");
        String groupId = getIntent().getStringExtra("groupId");
        NoticeGroup.staticGroup = groupId;

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
        ImageView menu=findViewById(R.id.menu);
        ImageView back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(NoticeGroup.this,menu);
                popupMenu.getMenuInflater().inflate(R.menu.admin_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(NoticeGroup.this,"clicked"+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = String.valueOf(message.getText());
                if(text.equals("")) {
                    Toast.makeText(NoticeGroup.this, "Type message", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getDefault());

                // Get the current time
                Date currentTime = new Date();

                // Format the current time to a string
                String formattedTime = sdf.format(currentTime);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference noticeCol = db.collection("Notice");
                Map<String, String> message = new HashMap<>();
                String uuid = UUID.randomUUID().toString();
                message.put("uuid", uuid);
                message.put("message", text);
                message.put("time", formattedTime);

                DocumentReference noticeDocument = noticeCol.document(groupId);
                CollectionReference messageCollection = noticeDocument.collection("Message");

                DocumentReference newDocument = messageCollection.document(uuid);
                newDocument.set(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document was successfully added to the courseLinkCollection
                                finish();
                                Intent intent = new Intent(NoticeGroup.this, NoticeGroup.class);
                                intent.putExtra("groupName", groupName);
                                intent.putExtra("adminId", adminId);
                                intent.putExtra("groupId", groupId);
                                startActivity(intent);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error
                            }
                        });
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MessageAdapter rVadapter =new MessageAdapter(this,messageItems,this);
        recyclerView.setAdapter(rVadapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }




    @Override
    public void OnItemClick(int position) {

    }
}

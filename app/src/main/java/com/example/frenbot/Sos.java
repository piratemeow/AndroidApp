package com.example.frenbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Sos extends AppCompatActivity {

    static final int CONTACT_PICKER_REQUEST_MESSAGE = 1;
    static final int CONTACT_PICKER_REQUEST_CALL = 2;
    static final int PERMISSION_REQUEST_CODE = 123;

    EditText messageNumberEditText;
    EditText callNumberEditText;
    EditText messageEditText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        messageNumberEditText = findViewById(R.id.messageNumberEditText);
        callNumberEditText = findViewById(R.id.callNumberEditText);
        messageEditText = findViewById(R.id.messageEditText);
        mAuth = FirebaseAuth.getInstance();

        checkAndRequestPermissions();

        Button selectMessageNumberButton = findViewById(R.id.selectMessageNumberButton);
        selectMessageNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactsForMessage(CONTACT_PICKER_REQUEST_MESSAGE);
            }
        });

        Button selectCallNumberButton = findViewById(R.id.selectCallNumberButton);
        selectCallNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactsForMessage(CONTACT_PICKER_REQUEST_CALL);
            }
        });
    }

    private void openContactsForMessage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACT_PICKER_REQUEST_MESSAGE) {
                handleContactPickResult(data, messageNumberEditText);
            } else if (requestCode == CONTACT_PICKER_REQUEST_CALL) {
                handleContactPickResult(data, callNumberEditText);
            }
        }
    }

    private void handleContactPickResult(Intent data, EditText editText) {
        Uri contactUri = data.getData();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = cursor.getString(numberColumnIndex);
            editText.setText(phoneNumber);
            cursor.close();
        }
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public void sendMessageAndCall(View view) {
        System.out.println("calling the handler");
        String messageNumber = messageNumberEditText.getText().toString();
        String callNumber = callNumberEditText.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance("https://frenbot-ebcbe-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("users");
            SOSInfo sosInfo = new SOSInfo(messageEditText.getText().toString(), messageNumberEditText.getText().toString(), callNumberEditText.getText().toString());

            usersRef.child(userId).child("sosInfo").setValue(sosInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Sos.this, "database updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Sos.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        // Check if messageNumber and callNumber are not empty
        if (!messageNumber.isEmpty() && !callNumber.isEmpty()) {
            sendSms(messageNumber, messageEditText.getText().toString());
            makeCall(callNumber);
        } else if(!messageNumber.isEmpty()){
            sendSms(messageNumber, messageEditText.getText().toString());
        } else if(!callNumber.isEmpty()) {
            makeCall(callNumber);
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
            Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Message sending failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void makeCall(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Call initiation failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

class SOSInfo {
    String message; String messageNumber; String callNumber;
    public SOSInfo(String message, String messageNumber, String callNumber) {
        this.message = message;
        this.messageNumber = messageNumber;
        this.callNumber = callNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public SOSInfo() {
    }
}
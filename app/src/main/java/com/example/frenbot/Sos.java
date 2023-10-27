package com.example.frenbot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sos extends AppCompatActivity implements SensorEventListener {

    static final int CONTACT_PICKER_REQUEST_MESSAGE = 1;
    static final int CONTACT_PICKER_REQUEST_CALL = 2;
    static final int PERMISSION_REQUEST_CODE = 123;

    EditText messageNumberEditText;
    EditText callNumberEditText;
    EditText messageEditText;
    FirebaseAuth mAuth;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isAccelerometerAvailable, isNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ, shakeThreshold = 10f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        messageNumberEditText = findViewById(R.id.messageNumberEditText);
        callNumberEditText = findViewById(R.id.callNumberEditText);
        messageEditText = findViewById(R.id.messageEditText);
        mAuth = FirebaseAuth.getInstance();
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            isAccelerometerAvailable = true;
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            isAccelerometerAvailable = false;
        }

        checkAndRequestPermissions();

        ImageView selectMessageNumberButton = findViewById(R.id.selectMessageNumberButton);
        selectMessageNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactsForMessage(CONTACT_PICKER_REQUEST_MESSAGE);
            }
        });

        ImageView selectCallNumberButton = findViewById(R.id.selectCallNumberButton);
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

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference sosCollection = db.collection("SosInfo");

            DocumentReference sosDocument = sosCollection.document(userId);

            // Store user data in the document.
            SOSInfo sosInfo = new SOSInfo(messageEditText.getText().toString(), messageNumberEditText.getText().toString(), callNumberEditText.getText().toString());

            Map<String, Object> sosInfoMap = new HashMap<>();
            sosInfoMap.put("message", sosInfo.getMessage());
            sosInfoMap.put("messageNumber", sosInfo.getMessageNumber());
            sosInfoMap.put("callNumber", sosInfo.getCallNumber());
            // Add other user-related data as needed.

            // Set the data in the document.
            sosDocument.set(sosInfoMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // User data has been successfully added.
                            Toast.makeText(Sos.this, "database updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error.
                            Toast.makeText(Sos.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    });
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(isNotFirstTime) {
            float xDiff = Math.abs(currentX - lastX);
            float yDiff = Math.abs(currentY - lastY);
            float zDiff = Math.abs(currentZ - lastZ);

            if((xDiff > shakeThreshold && yDiff > shakeThreshold) || (xDiff > shakeThreshold && zDiff > shakeThreshold) || (zDiff > shakeThreshold && yDiff > shakeThreshold)) {
                // Get the current user's UID from Firebase Authentication
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String uid = currentUser.getUid();

                    // Reference to the "sosInfo" collection
                    CollectionReference sosInfoCollection = FirebaseFirestore.getInstance().collection("SosInfo");

                    // Query the collection for the document with the user's UID
                    sosInfoCollection.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Document exists, you can retrieve the data here
                                    String message = document.getString("message");
                                    String messageNumber = document.getString("messageNumber");
                                    String callNumber = document.getString("callNumber");

                                    System.out.println(message);
                                    System.out.println(messageNumber);
                                    System.out.println(callNumber);

                                    makeCall(callNumber);

                                    // Check if messageNumber and callNumber are not empty
                                    if (!messageNumber.isEmpty() && !callNumber.isEmpty()) {
                                        sendSms(messageNumber, message);
                                        makeCall(callNumber);
                                    } else if(!messageNumber.isEmpty()){
                                        sendSms(messageNumber, message);
                                    } else if(!callNumber.isEmpty()) {
                                        makeCall(callNumber);
                                    }

                                    // Do something with the retrieved data (e.g., display it)
                                } else {
                                    // Document does not exist for this user
                                    // You can handle this case as needed
                                    System.out.println("Document does not exist for this user");
                                }
                            } else {
                                // Handle errors
                                Exception e = task.getException();
                                if (e != null) {
                                    // Log or display the error
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }

            }
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;
        isNotFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isAccelerometerAvailable) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerAvailable) {
            sensorManager.unregisterListener(this);
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
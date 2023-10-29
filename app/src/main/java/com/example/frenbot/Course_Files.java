package com.example.frenbot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Course_Files extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
//    FirebaseStorage storage;
//    StorageReference storageRef;
    public static String StoragePath;
    public static Uri FileUri;
    public static String Type;
    public static String FileExtension;
    FloatingActionButton addFile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_files);
        ImageView back=findViewById(R.id.back);
        String uuid = getIntent().getStringExtra("uuid");
        addFile=findViewById(R.id.add_File);

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a file picker dialog to allow the user to select files
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/pdf", "text/*", "image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"});

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a file"), REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle exception if no file picker is available
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        List<FileItem> fileItems = new ArrayList<>();

        // Find the RecyclerView and set its adapter
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerViewFiles = findViewById(R.id.recyclerViewFiles);
        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(this));
        FileAdapter rVadapter =new FileAdapter(this,fileItems,this);
        recyclerViewFiles.setAdapter(rVadapter);
        recyclerViewFiles.setAdapter(new FileAdapter(fileItems));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // The user has selected a file
            if (data != null) {
                Uri fileUri = data.getData();

                if (fileUri != null) {
                    String type;
                    String mimeType = getContentResolver().getType(fileUri);

                    // Determine the file extension from the file name or MIME type
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());

                    // Set up the Firebase Storage reference based on the file type
                    String storagePath = "files/";
                    
                    if (mimeType != null) {
                        if (mimeType.startsWith("image")) {
                            storagePath += "images/";
                            type = "img";
                        } else if (mimeType.equals("application/pdf")) {
                            storagePath += "pdfs/";
                            type = "pdf";
                        } else if (mimeType.startsWith("text")) {
                            storagePath += "texts/";
                            type = "txt";
                        } else if (mimeType.equals("application/msword") || mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                            // Handle "doc" or "docx" files
                            storagePath += "docs/";
                            type = "doc";
                        } else {
                            type = null;
                            // Handle other file types if needed
                            // You can show a message to the user indicating that this file type is not supported
                            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        type = null;
                        // Handle the case when MIME type is null
                    }
                    Course_Files.FileExtension = fileExtension;
                    Course_Files.FileUri = fileUri;
                    Course_Files.Type = type;
                    Course_Files.StoragePath = storagePath;
                    Intent intent = new Intent(Course_Files.this, AddCourseFile.class);
//                    intent.putExtra("type", type);
//                    intent.putExtra("fileUri", fileUri);
//                    intent.putExtra("storagePath", storagePath);
//                    intent.putExtra("fileExtension", fileExtension);
                    startActivity(intent);


//                    if(AddCourseFile.fileName != null) {
//                        StorageReference fileRef = storageRef.child(storagePath + UUID.randomUUID().toString() + "/" + AddCourseFile.fileName + "." + fileExtension);
//                        UploadTask uploadTask = fileRef.putFile(fileUri);
//                        uploadTask.addOnSuccessListener(taskSnapshot -> {
//                            fileRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
//                                String downloadURL = downloadUrl.toString();
//                                // Now you can save the downloadURL to Firestore or perform other actions.
//                                // File uploaded successfully
//                                Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
//
//                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                if (user != null) {
//                                    String userId = user.getUid();
//
//                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                    DocumentReference userDocument = db.collection("Users").document(userId);
//                                    CollectionReference coursesCollection = userDocument.collection("Course");
//
//                                    DocumentReference courseDocument = coursesCollection.document(Course_Details.courseUUID);
//                                    CollectionReference courseFileCollection = courseDocument.collection("courseFileCollection");
//
//                                    String fileId = UUID.randomUUID().toString();
//                                    Map<String, String> courseFile = new HashMap<>();
//                                    courseFile.put("title", AddCourseFile.fileName);
//                                    courseFile.put("downloadUri", downloadURL);
//                                    courseFile.put("uuid", fileId);
//                                    courseFile.put("fileType", type);
//
//                                    DocumentReference newLinkDocument = courseFileCollection.document(fileId);
//
//                                    newLinkDocument.set(courseFile)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    // Document was successfully added to the courseLinkCollection
//                                                    Toast.makeText(Course_Files.this, "url stored", Toast.LENGTH_SHORT).show();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    // Handle the error
//                                                }
//                                            });
//
//                                }
//                            });
//
//                        }).addOnFailureListener(exception -> {
//                            // Handle unsuccessful uploads
//                            Toast.makeText(this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                        });
//                    }
                }
            }
        }
    }
}

package com.example.frenbot;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import android.content.Context;
//
//public class courseRVadapter extends RecyclerView.Adapter<courseRVadapter.MyViewHolder> {
//
//    Context context;
//    ArrayList<coursemodel> coursemodels;
//
//    public courseRVadapter(Context context, ArrayList<coursemodel> coursemodels){
//        this.context=context;
//        this.coursemodels=coursemodels;
//    }
//    @NonNull
//    @Override
//    public courseRVadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater= LayoutInflater.from(context);
//        View view=inflater.inflate(R.layout.courses_rview,parent,false);
//
//        return new courseRVadapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull courseRVadapter.MyViewHolder holder, int position) {
//
//        holder.course.setText(coursemodels.get(position).getcourse());
//        holder.id.setText(coursemodels.get(position).getid());
//        holder.instructor.setText(coursemodels.get(position).getinstructor());
//    }
//
//    @Override
//    public int getItemCount() {
//        return coursemodels.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//        TextView course,id,instructor;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            course=itemView.findViewById(R.id.course);
//            id=itemView.findViewById(R.id.id);
//            instructor=itemView.findViewById(R.id.instructor);
//        }
//    }
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class courseRVadapter extends RecyclerView.Adapter<courseRVadapter.MyViewHolder> {

    private final RCViewInterface rcViewInterface;
    private Context context;
    static ArrayList<coursemodel> coursemodels;

    public courseRVadapter(Context context, ArrayList<coursemodel> coursemodels,RCViewInterface rcViewInterface) {
        this.context = context;
        this.coursemodels = coursemodels;
        this.rcViewInterface=rcViewInterface;
        fetchDataFromFirestore(); // Fetch data from Firestore when the adapter is created
    }

    private void fetchDataFromFirestore() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocument = db.collection("Users").document(userId);
            CollectionReference coursesCollection = userDocument.collection("Course");

            coursesCollection.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            coursemodels.clear(); // Clear existing data before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String course = document.getString("title");
                                String id = document.getString("id");
                                String instructor = document.getString("instructor");
                                String uuid = document.getString("uuid");

                                coursemodel courseModel = new coursemodel(course, id, instructor, uuid);
                                coursemodels.add(courseModel);
                            }
                            notifyDataSetChanged(); // Notify the RecyclerView to refresh
                        } else {
                            // Handle the error
                        }
                    });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.courses_rview, parent, false);
        return new MyViewHolder(view,rcViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.course.setText(coursemodels.get(position).getcourse());
        holder.id.setText(coursemodels.get(position).getid());
        holder.instructor.setText(coursemodels.get(position).getinstructor());
    }

    @Override
    public int getItemCount() {
        return coursemodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView course, id, instructor;

        public MyViewHolder(@NonNull View itemView, RCViewInterface rcViewInterface) {
            super(itemView);
            course = itemView.findViewById(R.id.course);
            id = itemView.findViewById(R.id.id);
            instructor = itemView.findViewById(R.id.instructor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rcViewInterface!=null){
                        int pos=getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION){
                            rcViewInterface.OnItemClick(pos);
                        }
                    }
                }
            });
        }
    }

}

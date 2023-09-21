package com.example.frenbot;

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
//public class eventRVadapter extends RecyclerView.Adapter<eventRVadapter.MyViewHolder> {
//
//    Context context;
//    ArrayList<eventmodel> eventmodels;
//
//    public eventRVadapter(Context context, ArrayList<eventmodel> eventmodels){
//        this.context=context;
//        this.eventmodels=eventmodels;
//    }
//    @NonNull
//    @Override
//    public eventRVadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater= LayoutInflater.from(context);
//        View view=inflater.inflate(R.layout.event_rview,parent,false);
//
//        return new eventRVadapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull eventRVadapter.MyViewHolder holder, int position) {
//
//        holder.title.setText(eventmodels.get(position).getTitle());
//        holder.time.setText(eventmodels.get(position).getTime());
//        holder.place.setText(eventmodels.get(position).getPlace());
//    }
//
//    @Override
//    public int getItemCount() {
//        return eventmodels.size();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder{
//
//        TextView title,time,place;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            title=itemView.findViewById(R.id.title);
//            time=itemView.findViewById(R.id.duration);
//            place=itemView.findViewById(R.id.venue);
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class eventRVadapter extends RecyclerView.Adapter<eventRVadapter.MyViewHolder> {

    private Context context;
    private ArrayList<eventmodel> eventmodels;

    public eventRVadapter(Context context, ArrayList<eventmodel> eventmodels) {
        this.context = context;
        this.eventmodels = eventmodels;
        fetchDataFromFirestore(); // Fetch data from Firestore when the adapter is created
    }

    private void fetchDataFromFirestore() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocument = db.collection("Users").document(userId);
            CollectionReference eventsCollection = userDocument.collection("Events");

            eventsCollection.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            eventmodels.clear(); // Clear existing data before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String time = document.getString("time");
                                String place = document.getString("location");

                                eventmodel eventModel = new eventmodel(title, time, place);
                                eventmodels.add(eventModel);
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
        View view = inflater.inflate(R.layout.event_rview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(eventmodels.get(position).getTitle());
        holder.time.setText(eventmodels.get(position).getTime());
        holder.place.setText(eventmodels.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return eventmodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, time, place;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.duration);
            place = itemView.findViewById(R.id.venue);
        }
    }
}


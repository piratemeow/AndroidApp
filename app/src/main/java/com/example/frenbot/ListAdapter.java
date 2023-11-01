package com.example.frenbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    List<ListItem> listItems;

    public ListAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
        fetchDataFromFirestore();
    }

    public ListAdapter(ListClass courseFiles, List<ListItem> fileItems, ListClass courseFiles1) {

    }

    @NonNull
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.MyViewHolder holder, int position) {
        ListItem item = listItems.get(position);
        holder.titleTextView.setText(item.name);
        if (!Objects.equals(listItems.get(position).profilePic, "") && listItems.get(position).profilePic != null) {
            Picasso.get().load(listItems.get(position).profilePic).into(holder.icon);
        }

        // Set an onClickListener to open the link in a browser
        holder.itemView.setOnClickListener(v -> {
            listItems.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private void fetchDataFromFirestore() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference users = db.collection("Users");
            CollectionReference noticeCollection = db.collection("Notice");
            DocumentReference noticeGp = noticeCollection.document(NoticeGroup.staticGroup);
            CollectionReference allMember = noticeGp.collection("Member");
            allMember.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listItems.clear(); // Clear existing data before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String profilePic = document.getString("profilePic");
                                String uuid = document.getString("userId");

                                ListItem fileItem = new ListItem(uuid,name,profilePic);
                                listItems.add(fileItem);
                            }
                            notifyDataSetChanged(); // Notify the RecyclerView to refresh
                        } else {
                            // Handle the error
                        }
                    });
            List<ListItem> temp = new ArrayList<>();
            if(NoticeGroup.flag == 2) {
                users.get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                listItems.clear(); // Clear existing data before adding new data
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String name = document.getString("name");
                                    String profilePic = document.getString("profilePic");
                                    String uuid = document.getString("userId");

                                    ListItem fileItem = new ListItem(uuid, name, profilePic);
                                    temp.add(fileItem);
                                }
                                notifyDataSetChanged(); // Notify the RecyclerView to refresh
                            } else {
                                // Handle the error
                            }
                        });

            }

        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}

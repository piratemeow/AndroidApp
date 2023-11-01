package com.example.frenbot;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

    private List<MessageItem> messageItems;
    private Context context;
    private RCViewInterface rcViewInterface;

    public MessageAdapter(Context context, ArrayList<MessageItem> messageItemArrayList, RCViewInterface rcViewInterface) {
        this.context = context;
        this.messageItems = messageItemArrayList;
        this.rcViewInterface=rcViewInterface;
        fetchDataFromFirestore(); // Fetch data from Firestore when the adapter is created
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notice, parent, false);
        return new MyViewHolder(view,rcViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        MessageItem item = messageItems.get(position);
        holder.messageText.setText(item.message);
        holder.messageTime.setText(item.timeStamp);
    }

    @Override
    public int getItemCount() {
        return messageItems.size();
    }

    private void fetchDataFromFirestore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference noticeCollection = db.collection("Notice");
        DocumentReference noticeGroup = noticeCollection.document(NoticeGroup.staticGroup);
        CollectionReference chatCollection = noticeGroup.collection("Message");

        Comparator<MessageItem> timestampComparator = new Comparator<MessageItem>() {
            @Override
            public int compare(MessageItem message1, MessageItem message2) {
                // Parse timestamps and compare them in reverse order
                return message2.timeStamp.compareTo(message1.timeStamp);
            }
        };

        chatCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        messageItems.clear(); // Clear existing data before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String message = document.getString("message");
                            String time = document.getString("time");
                            String uuid = document.getString("uuid");

                            MessageItem messageItem = new MessageItem(message, time, uuid);
                            messageItems.add(messageItem);
                        }
                        Collections.sort(messageItems, timestampComparator);
                        notifyDataSetChanged(); // Notify the RecyclerView to refresh
                    } else {
                        // Handle the error
                    }
                });
        
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;

        public MyViewHolder(@NonNull View itemView, RCViewInterface rcViewInterface) {
            super(itemView);
            messageText = itemView.findViewById(R.id.noticeTextView);
            messageTime = itemView.findViewById(R.id.timestampTextView);

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

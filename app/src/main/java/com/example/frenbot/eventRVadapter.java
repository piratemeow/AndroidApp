package com.example.frenbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;

public class eventRVadapter extends RecyclerView.Adapter<eventRVadapter.MyViewHolder> {

    Context context;
    ArrayList<eventmodel> eventmodels;

    public eventRVadapter(Context context, ArrayList<eventmodel> eventmodels){
        this.context=context;
        this.eventmodels=eventmodels;
    }
    @NonNull
    @Override
    public eventRVadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.event_rview,parent,false);

        return new eventRVadapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull eventRVadapter.MyViewHolder holder, int position) {

        holder.title.setText(eventmodels.get(position).getTitle());
        holder.time.setText(eventmodels.get(position).getTime());
        holder.place.setText(eventmodels.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return eventmodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title,time,place;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.duration);
            place=itemView.findViewById(R.id.venue);
        }
    }
}

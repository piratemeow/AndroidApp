package com.example.frenbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;

public class courseRVadapter extends RecyclerView.Adapter<courseRVadapter.MyViewHolder> {

    Context context;
    ArrayList<coursemodel> coursemodels;

    public courseRVadapter(Context context, ArrayList<coursemodel> coursemodels){
        this.context=context;
        this.coursemodels=coursemodels;
    }
    @NonNull
    @Override
    public courseRVadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.courses_rview,parent,false);

        return new courseRVadapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull courseRVadapter.MyViewHolder holder, int position) {

        holder.course.setText(coursemodels.get(position).getcourse());
        holder.id.setText(coursemodels.get(position).getid());
        holder.instructor.setText(coursemodels.get(position).getinstructor());
    }

    @Override
    public int getItemCount() {
        return coursemodels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView course,id,instructor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course=itemView.findViewById(R.id.course);
            id=itemView.findViewById(R.id.id);
            instructor=itemView.findViewById(R.id.instructor);
        }
    }
}

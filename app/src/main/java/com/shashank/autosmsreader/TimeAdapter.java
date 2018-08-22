package com.shashank.autosmsreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    Context context;
    List<TimeInterval> timeIntervals;
    SharedPreferences shref;

    public TimeAdapter(Context context,List<TimeInterval> timeIntervals){
        this.context=context;
        this.timeIntervals=timeIntervals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.time_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.timeInterval.setText(timeIntervals.get(position).getTimeInterval());
    }

    @Override
    public int getItemCount() {
        return timeIntervals.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeInterval;
        Button deleteTime;

        public ViewHolder(View itemView) {
            super(itemView);

            timeInterval=(TextView) itemView.findViewById(R.id.timeInterval);
            deleteTime=(Button) itemView.findViewById(R.id.deleteTime);
            deleteTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeIntervals.remove(getAdapterPosition());
                    notifyDataSetChanged();

                    SharedPreferences.Editor editor;
                    shref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(timeIntervals);
                    editor = shref.edit();
                    editor.remove("times").commit();
                    editor.putString("times",json);
                    editor.commit();
                }
            });

        }
    }
}

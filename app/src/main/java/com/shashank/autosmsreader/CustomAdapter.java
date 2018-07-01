package com.shashank.autosmsreader;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList<Messages> messageList;

   public CustomAdapter(Context context, ArrayList<Messages> messageList){
       this.context=context;
       this.messageList=messageList;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.senderNameTextView.setText(messageList.get(position).getSenderName());
        holder.messageBodyTextView.setText(messageList.get(position).getMessageBody());
        holder.dateTextView.setText(messageList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

       TextView senderNameTextView;
       TextView messageBodyTextView;
       TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            senderNameTextView=itemView.findViewById(R.id.senderNameTextView);
            messageBodyTextView=itemView.findViewById(R.id.messageBodyTextView);
            dateTextView=itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ReadMessage.class);
                    intent.putExtra("senderName",messageList.get(getAdapterPosition()).getSenderName());
                    intent.putExtra("messageBody",messageList.get(getAdapterPosition()).getMessageBody());
                    context.startActivity(intent);
                }
            });
        }
    }
}

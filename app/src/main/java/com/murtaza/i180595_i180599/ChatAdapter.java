package com.murtaza.i180595_i180599;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    List<Message> list;
    Context context;

    public ChatAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        holder.message.setText(list.get(position).getMessage());
        holder.time.setText(list.get(position).getTime());

        CurrentUser currentUser = new CurrentUser();
        if (list.get(holder.getAdapterPosition()).getFrom().equals(currentUser.getUser())) {
            holder.constraintLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle2));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.constraintLayout.getLayoutParams();
            params.leftMargin = 750;
        }
        else {
            holder.constraintLayout.setBackground(context.getResources().getDrawable(R.drawable.rounded_rectangle));
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditMessage.class);
                intent.putExtra("Id", list.get(holder.getAdapterPosition()).getId());
                intent.putExtra("Text", list.get(holder.getAdapterPosition()).getMessage());
                intent.putExtra("Time", list.get(holder.getAdapterPosition()).getTime());
                intent.putExtra("Username", list.get(holder.getAdapterPosition()).getUsername());
                intent.putExtra("To", list.get(holder.getAdapterPosition()).getTo());
                intent.putExtra("From", list.get(holder.getAdapterPosition()).getFrom());
                context.startActivity(intent);
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(context, DeleteMessage.class);
                intent.putExtra("Id", list.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView message, time;
        View view;
        ConstraintLayout constraintLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            constraintLayout = itemView.findViewById(R.id.cl);
            view = itemView;
        }
    }

    public void filterListChat(List<Message> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}

package com.murtaza.i180595_i180599;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.CallHistoryViewHolder> {

    List<CallRecord> list;
    Context context;

    public CallHistoryAdapter(List<CallRecord> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.callrecord, parent, false);
        return new CallHistoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CallHistoryViewHolder holder, int position) {
        holder.name.setText(list.get(holder.getAdapterPosition()).getName());
        holder.time.setText(list.get(holder.getAdapterPosition()).getTime());
        holder.profile.setImageResource(list.get(holder.getAdapterPosition()).getImage());
        if (list.get(position).getStatus().equals("incoming")) {
            holder.status_image.setImageResource(R.drawable.missed_call);
        }
        if (list.get(position).getStatus().equals("outgoing")) {
            holder.status_image.setImageResource(R.drawable.outgoing);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Screen9.class);
                intent.putExtra("Name", list.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Time", list.get(holder.getAdapterPosition()).getTime());
                intent.putExtra("Image", list.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Status", list.get(holder.getAdapterPosition()).getStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CallHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;
        ImageView profile, status_image;
        View view;

        public CallHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            profile = itemView.findViewById(R.id.profpic);
            status_image = itemView.findViewById(R.id.status_img);
            view = itemView;
        }
    }
}

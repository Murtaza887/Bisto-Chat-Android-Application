package com.murtaza.i180595_i180599;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {

    List<Contact> contact;
    Context context;

    public RvAdapter(List<Contact> contact, Context context) {
        this.contact = contact;
        this.context = context;
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        return new RvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder holder, int position) {
        holder.name.setText(contact.get(holder.getAdapterPosition()).getName());
        holder.last_active.setText(contact.get(holder.getAdapterPosition()).getLast_active());
        holder.image.setImageResource(contact.get(holder.getAdapterPosition()).getImage());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Screen5.class);
                intent.putExtra("Index", holder.getAdapterPosition());
                intent.putExtra("Name", contact.get(holder.getAdapterPosition()).getName());
                intent.putExtra("Last Active", contact.get(holder.getAdapterPosition()).getLast_active());
                intent.putExtra("Image", contact.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Phone Number", contact.get(holder.getAdapterPosition()).getPhone());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.contact.size();
    }

    public static class RvViewHolder extends RecyclerView.ViewHolder {
        TextView name, last_message, last_active;
        ImageView image;
        View view;

        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            last_active = itemView.findViewById(R.id.last_active);
            image = itemView.findViewById(R.id.profpic);
            view = itemView;
        }
    }

    public void filterList(ArrayList<Contact> filteredList) {
        contact = filteredList;
        notifyDataSetChanged();
    }
}

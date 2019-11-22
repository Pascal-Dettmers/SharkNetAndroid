package com.htw.s0551733.sharnetpki.recyclerViews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;

import java.util.ArrayList;

public class PublicKeyRecyclerAdapter extends RecyclerView.Adapter<PublicKeyRecyclerAdapter.CustomViewHolder> {

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView alias;
        OnKeyListener onKeyListener;

        public CustomViewHolder(View itemView, OnKeyListener onKeyListener) {
            super(itemView);
            this.alias = itemView.findViewById(R.id.tvKeyAlias);
            this.onKeyListener = onKeyListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onKeyListener.onKeyClick(getAdapterPosition());
        }
    }

    private ArrayList<SharkNetKey> data;
    private OnKeyListener onKeyListener;

    public PublicKeyRecyclerAdapter(ArrayList<SharkNetKey> data, OnKeyListener onKeyListener) {
        this.data = data;
        this.onKeyListener = onKeyListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_public_key_fragment, parent, false), this.onKeyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.alias.setText(data.get(position).getAlias());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnKeyListener {
        void onKeyClick(int position);
    }
}

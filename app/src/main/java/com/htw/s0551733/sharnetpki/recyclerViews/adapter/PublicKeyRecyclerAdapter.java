package com.htw.s0551733.sharnetpki.recyclerViews.adapter;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.SharknetPublicKey;

public class PublicKeyRecyclerAdapter extends RecyclerView.Adapter<PublicKeyRecyclerAdapter.CustomViewHolder> {

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView alias;
        TextView publicKey;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.alias = itemView.findViewById(R.id.tvKeyAlias);
            this.publicKey = itemView.findViewById(R.id.text_view_public_key);
        }

    }

    private HashSet<SharknetPublicKey> data;

    public PublicKeyRecyclerAdapter(HashSet<SharknetPublicKey> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_public_key_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        List<SharknetPublicKey> dataList = new ArrayList<>(data);
        holder.alias.setText(dataList.get(position).getAlias());
        String publicKeyEncodedToString = Base64.encodeToString(dataList.get(position).getPublicKey().getEncoded(), Base64.DEFAULT);
        holder.publicKey.setText(publicKeyEncodedToString.substring(44));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

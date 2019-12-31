package com.htw.s0551733.sharnetpki.recyclerViews.adapter;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.recyclerViews.clickListener.ClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

public class PublicKeyRecyclerAdapter extends RecyclerView.Adapter<PublicKeyRecyclerAdapter.CustomViewHolder> {

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView alias;
        TextView publicKey;
        Button sendKey;
        Button deleteKey;
        private WeakReference<ClickListener> listenerRef;

        public CustomViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.alias = itemView.findViewById(R.id.tvKeyAlias);
            this.publicKey = itemView.findViewById(R.id.text_view_public_key);
            this.sendKey = itemView.findViewById(R.id.material_button_send);
            this.deleteKey = itemView.findViewById(R.id.material_button_delete);
            listenerRef = new WeakReference<>(listener);


            itemView.setOnClickListener(this);
            sendKey.setOnClickListener(this);
            deleteKey.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == sendKey.getId()) {
                listenerRef.get().onSendClicked(getAdapterPosition());
            } else {
                listenerRef.get().onDeleteClicked(getAdapterPosition());
                Toast.makeText(v.getContext(), "DELETED", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private HashSet<SharkNetPublicKey> data;
    private final ClickListener listener;

    public PublicKeyRecyclerAdapter(HashSet<SharkNetPublicKey> data, ClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_public_key_fragment, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        List<SharkNetPublicKey> dataList = new ArrayList<>(data);
        holder.alias.setText(dataList.get(position).getOwner().getAlias());
        String publicKeyEncodedToString = Base64.encodeToString(dataList.get(position).getPublicKey().getEncoded(), Base64.DEFAULT);
        holder.publicKey.setText(publicKeyEncodedToString.substring(44));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

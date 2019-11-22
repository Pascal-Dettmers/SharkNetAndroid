package com.htw.s0551733.sharnetpki.recyclerViews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCertification;

import java.util.ArrayList;

public class CertificationRecyclerAdapter extends RecyclerView.Adapter<CertificationRecyclerAdapter.CustomViewHolder> {

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView alias;
        TextView signer;

        OnCertificationClickListener onCertificationClickListener;

        public CustomViewHolder(View itemView, OnCertificationClickListener onCertificationClickListener) {
            super(itemView);
            this.alias = itemView.findViewById(R.id.cert_public_key);
            this.signer = itemView.findViewById(R.id.cert_signer);
            this.onCertificationClickListener = onCertificationClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCertificationClickListener.onKeyClick(getAdapterPosition());
        }
    }

    private ArrayList<SharkNetCertification> data;
    private OnCertificationClickListener onCertificationClickListener;

    public CertificationRecyclerAdapter(ArrayList<SharkNetCertification> data, OnCertificationClickListener onCertificationClickListener) {
        this.data = data;
        this.onCertificationClickListener = onCertificationClickListener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_certification_fragment, parent, false), onCertificationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.alias.setText(data.get(position).getAlias());
        holder.signer.setText(data.get(position).getSigner().getAlias());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnCertificationClickListener {
        void onKeyClick(int position);
    }
}

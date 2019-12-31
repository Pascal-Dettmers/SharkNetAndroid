package com.htw.s0551733.sharnetpki.recyclerViews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCert;
import com.htw.s0551733.sharnetpki.recyclerViews.clickListener.ClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

public class CertificationRecyclerAdapter extends RecyclerView.Adapter<CertificationRecyclerAdapter.CustomViewHolder> {

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView signer;
        TextView subject;
        Button deleteCert;
        private WeakReference<ClickListener> listenerRef;


        public CustomViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.signer = itemView.findViewById(R.id.tv_certificate_signer);
            this.subject = itemView.findViewById(R.id.tv_certificate_subject);
            this.deleteCert = itemView.findViewById(R.id.material_button_delete_certificate);
            listenerRef = new WeakReference<>(listener);

            itemView.setOnClickListener(this);
            deleteCert.setOnClickListener(this);
            
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == deleteCert.getId()) {
                listenerRef.get().onDeleteClicked(getAdapterPosition());
                Toast.makeText(v.getContext(), "DELETED", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private HashSet<SharkNetCertificate> data;
    private final ClickListener listener;

    public CertificationRecyclerAdapter(HashSet<SharkNetCertificate> data, ClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_certification_fragment, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        List<SharkNetCertificate> dataList = new ArrayList<>(data);

        holder.subject.setText(dataList.get(position).getSubject().getAlias());
        holder.signer.setText(dataList.get(position).getSigner().getAlias());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

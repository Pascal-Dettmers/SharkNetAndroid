package com.htw.s0551733.sharnetpki.pager.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.nfc.receive.NfcCallback;
import com.htw.s0551733.sharnetpki.pager.SharkNetPagerAdapter;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCert;
import com.htw.s0551733.sharnetpki.recyclerViews.adapter.CertificationRecyclerAdapter;
import com.htw.s0551733.sharnetpki.recyclerViews.clickListener.ClickListener;
import com.htw.s0551733.sharnetpki.storage.datastore.DataStorage;
import com.htw.s0551733.sharnetpki.util.SharedPreferencesHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import main.de.htw.berlin.s0551733.sharknetpki.SharkNetPKI;
import main.de.htw.berlin.s0551733.sharknetpki.VerifySignaturResult;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetCertificate;

public class CertificationsFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private DataStorage storage;
    HashSet<SharkNetCertificate> sharkNetCertificates;

    public CertificationsFragment() {
        // Required empty public constructor
    }

    public static CertificationsFragment newInstance() {
        CertificationsFragment fragment = new CertificationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_certifications, container, false);
        storage = new DataStorage(new SharedPreferencesHandler(getActivity()));
        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        // recycler view
        recyclerView = view.findViewById(R.id.fragment_certification_tab_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initRecyclerViewAdapter();
    }

    private void initRecyclerViewAdapter() {
        this.sharkNetCertificates = SharkNetPKI.getInstance().getSharkNetCertificates();
        if (this.sharkNetCertificates != null) {
            recyclerViewAdapter = new CertificationRecyclerAdapter(this.sharkNetCertificates, new ClickListener() {
                @Override
                public void onSendClicked(int position) {
                    List<SharkNetCertificate> list = new ArrayList<>(sharkNetCertificates);
                    SharkNetCertificate sharkNetCertificate = list.get(position);

                    if(SharkNetPKI.getInstance().getUsers().contains(sharkNetCertificate.getSigner())){
                        if(SharkNetPKI.getInstance().verifySignature(sharkNetCertificate.getCertificate(),SharkNetPKI.getInstance().getPublicKey(sharkNetCertificate.getSigner().getUuid())) == VerifySignaturResult.VERIFIED){

                            SharkNetCert verifiedCert = (SharkNetCert) list.get(position);
                            verifiedCert.setVerified(true);

                            storage.deleteSharkNetCertificate(sharkNetCertificate);
                            sharkNetCertificates.remove(list.get(position));
                            SharkNetPKI.getInstance().removeCertificate(list.get(position));

                            storage.getCertificateSet().add(verifiedCert);
                            sharkNetCertificates.add(verifiedCert);
                            SharkNetPKI.getInstance().addCertificate(verifiedCert);

                            updateRecyclerView();

                            new MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Succesfull")
                                    .setMessage("Certificate succesfull verified!")
                                    .setPositiveButton("Ok",null)
                                    .setCancelable(false)
                                    .show();

                        } else {
                            Toast.makeText(getContext(), "Error - compromised certificate ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error - fitting public key not found ", Toast.LENGTH_SHORT).show();
                    }

                    updateRecyclerView();

                }

                @Override
                public void onDeleteClicked(int position) {
                    List<SharkNetCertificate> list = new ArrayList<>(sharkNetCertificates);
                    SharkNetCertificate sharkNetCertificate = list.get(position);
                    storage.deleteSharkNetCertificate(sharkNetCertificate);
                    sharkNetCertificates.remove(list.get(position));
                    SharkNetPKI.getInstance().removeCertificate(list.get(position));
                    updateRecyclerView();
                }
            });
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {

        }
    }


    public void updateRecyclerView() {
        HashSet<SharkNetCertificate> updatedCertList = SharkNetPKI.getInstance().getSharkNetCertificates();
        this.sharkNetCertificates.addAll(updatedCertList);
        this.recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

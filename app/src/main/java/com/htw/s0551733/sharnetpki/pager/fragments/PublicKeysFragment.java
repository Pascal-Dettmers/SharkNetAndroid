package com.htw.s0551733.sharnetpki.pager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.nfc.receive.NfcCallback;
import com.htw.s0551733.sharnetpki.recyclerViews.adapter.PublicKeyRecyclerAdapter;

import java.util.HashSet;
import java.util.Set;

import main.de.htw.berlin.s0551733.sharknetpki.SharkNetPKI;
import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;

public class PublicKeysFragment extends Fragment implements NfcCallback {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    HashSet<SharkNetPublicKey> sharkNetKeys;

    public static final String TAG = "PublicKeysFragment";

    public PublicKeysFragment() {
        // Required empty public constructor
    }

    public static PublicKeysFragment newInstance() {
        PublicKeysFragment fragment = new PublicKeysFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public_keys, container, false);
        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.fragment_public_key_tab_recycler_view);
        // improve performance
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
        this.sharkNetKeys = SharkNetPKI.getInstance().getSharkNetPublicKeys();
        if (sharkNetKeys != null) {
            recyclerViewAdapter = new PublicKeyRecyclerAdapter(this.sharkNetKeys);
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {
            // TODO dummy data
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    public void updateRecyclerView() {
        HashSet<SharkNetPublicKey> updatedKeyList = SharkNetPKI.getInstance().getSharkNetPublicKeys();
//        this.sharkNetKeys.clear();
        this.sharkNetKeys.addAll(updatedKeyList);
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


    @Override
    public void onDataReceived() {
        updateRecyclerView();
    }
}

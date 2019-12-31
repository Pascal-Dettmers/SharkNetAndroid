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
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCert;
import com.htw.s0551733.sharnetpki.recyclerViews.adapter.CertificationRecyclerAdapter;

import java.util.ArrayList;

public class CertificationsFragment extends Fragment implements CertificationRecyclerAdapter.OnCertificationClickListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;

    ArrayList<SharkNetCert> certList;

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

        return view;
    }

    private void initRecyclerViewAdapter() {
        if (this.certList != null) {
            recyclerViewAdapter = new CertificationRecyclerAdapter(this.certList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {

        }

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
    public void onKeyClick(int position) {
//        Intent intent = new Intent(getActivity(), DetailViewCertificationActivity.class);
//        int itemPos = position;
//        intent.putExtra("ITEM_POS", itemPos);
//        startActivity(intent);
    }
}

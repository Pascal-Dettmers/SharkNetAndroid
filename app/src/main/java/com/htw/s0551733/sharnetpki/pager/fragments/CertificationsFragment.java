package com.htw.s0551733.sharnetpki.pager.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetCertification;
import com.htw.s0551733.sharnetpki.recyclerViews.adapter.CertificationRecyclerAdapter;
import com.htw.s0551733.sharnetpki.storage.SharedPreferencesHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CertificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CertificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CertificationsFragment extends Fragment implements CertificationRecyclerAdapter.OnCertificationClickListener {

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    SharedPreferencesHandler sharedPreferencesHandler;

    ArrayList<SharkNetCertification> certList;

    public CertificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CertificationsFragment.
     */
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

        // use a linear layout manager
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
            // todo empty recycler view layout add dummy data
//            ArrayList<SharkNetCertification> dummyData = new ArrayList<>();
//
//            dummyData.add(new SharkNetCertification("DummyAlias1", "Dummy UUID", "",
//                    new Signer("Dummy Signer Alias1", "Dummy uuid", "")));
//
//            dummyData.add(new SharkNetCertification("DummyAlias2", "Dummy UUID", "",
//                    new Signer("Dummy Signer Alias2", "Dummy uuid", "")));
//
//            recyclerViewAdapter = new CertificationRecyclerAdapter(dummyData, this);
//            recyclerView.setAdapter(recyclerViewAdapter);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onKeyClick(int position) {
//        Intent intent = new Intent(getActivity(), DetailViewCertificationActivity.class);
//        int itemPos = position;
//        intent.putExtra("ITEM_POS", itemPos);
//        startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

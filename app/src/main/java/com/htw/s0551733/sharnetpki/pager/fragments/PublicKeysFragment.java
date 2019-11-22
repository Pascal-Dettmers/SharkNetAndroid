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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htw.s0551733.sharnetpki.R;
import com.htw.s0551733.sharnetpki.util.Constants;
import com.htw.s0551733.sharnetpki.recyclerViews.adapter.PublicKeyRecyclerAdapter;
import com.htw.s0551733.sharnetpki.recyclerViews.SharkNetKey;
import com.htw.s0551733.sharnetpki.storage.SharedPreferencesHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PublicKeysFragment extends Fragment implements PublicKeyRecyclerAdapter.OnKeyListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;

    SharedPreferencesHandler sharedPreferencesHandler;

    ArrayList<SharkNetKey> keyList;

    private OnFragmentInteractionListener mListener;

    public PublicKeysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PublicKeysFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        sharedPreferencesHandler = new SharedPreferencesHandler(getActivity().getApplicationContext());
        this.keyList = getKeyList();

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
        if (this.keyList != null) {
            recyclerViewAdapter = new PublicKeyRecyclerAdapter(this.keyList, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }else {
            // TODO dummy data
        }
    }

    private ArrayList<SharkNetKey> getKeyList() {
        Gson gson = new Gson();
        Type typeOfKeylist = new TypeToken<ArrayList<SharkNetKey>>() {
        }.getType();

        String keylistInJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);

        if (keylistInJson != null) {
            ArrayList<SharkNetKey> keylist = gson.fromJson(keylistInJson, typeOfKeylist);
            return keylist;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<SharkNetKey> updatedKeyList = getKeyList();
        this.keyList.clear();
        this.keyList.addAll(updatedKeyList);
        this.recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onKeyClick(int position) {
//        Intent intent = new Intent(getActivity(), DetailViewActivity.class);
//        int itemPos = position;
//        intent.putExtra("ITEM_POS", itemPos);
//        startActivity(intent);
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

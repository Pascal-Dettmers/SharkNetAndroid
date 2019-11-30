package com.htw.s0551733.sharnetpki;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;


public class BottomNavigatorFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    private NavigationView navigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);
        navigationView = view.findViewById(R.id.navigation_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.identity_view:
                        startActivity(new Intent(getActivity(), IdentityActivity.class));
                        dismiss();
                        break;
                    case R.id.settings_view:
                        Toast.makeText(getContext(), "Settings View", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about_view:
                        Toast.makeText(getContext(), "About View", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
    }
}

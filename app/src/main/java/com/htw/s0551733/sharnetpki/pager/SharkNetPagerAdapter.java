package com.htw.s0551733.sharnetpki.pager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.htw.s0551733.sharnetpki.pager.fragments.CertificationsFragment;
import com.htw.s0551733.sharnetpki.pager.fragments.PublicKeysFragment;

/**
 * A simple pager adapter that represents 2 fragment objects, in
 * sequence.
 */
public class SharkNetPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * The number of pages
     */
    private static final int NUM_PAGES = 2;

    public SharkNetPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PublicKeysFragment.newInstance();
            case 1:
                return CertificationsFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pub Keys";
            case 1:
                return "Cert";
            default:
                return "";
        }
    }
}



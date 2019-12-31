package com.htw.s0551733.sharnetpki.pager;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.htw.s0551733.sharnetpki.pager.fragments.CertificationsFragment;
import com.htw.s0551733.sharnetpki.pager.fragments.PublicKeysFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple pager adapter that represents 2 fragment objects, in
 * sequence.
 */
public class SharkNetPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * The number of pages
     */
    private static final int NUM_PAGES = 2;

    private HashMap<Integer, Fragment> mPageReferenceMap;

    public SharkNetPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPageReferenceMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PublicKeysFragment publicKeysFragment = PublicKeysFragment.newInstance();
                mPageReferenceMap.put(position, publicKeysFragment);
                return publicKeysFragment;
            case 1:
                CertificationsFragment certificationsFragment = CertificationsFragment.newInstance();
                mPageReferenceMap.put(position, certificationsFragment);
                return certificationsFragment;
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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

}



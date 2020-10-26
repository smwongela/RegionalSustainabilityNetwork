package com.mwongela.regionalsustainabilitynetwork;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumberOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.mNumberOfTabs = numOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProjectFragment();
            case 1:
                return new EventFragment();
            case 2:
                return new ReportFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}

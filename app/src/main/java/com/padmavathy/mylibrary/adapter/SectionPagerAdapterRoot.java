package com.padmavathy.mylibrary.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapterRoot extends FragmentPagerAdapter {

    private Fragment fragment1;
    private Fragment fragment2;
    private int fragmentCount;

    public SectionPagerAdapterRoot(FragmentManager fm, Fragment fragment1, Fragment fragment2, int fragmentCount) {
        super(fm);
        this.fragment1 = fragment1;
        this.fragment2 = fragment2;
        this.fragmentCount = fragmentCount;
    }

    public SectionPagerAdapterRoot(FragmentManager fm, Fragment fragment1,int fragmentCount) {
        super(fm);
        this.fragment1 = fragment1;
        this.fragmentCount = fragmentCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment1;
            case 1:
                return fragment2;

        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentCount;
    }
}

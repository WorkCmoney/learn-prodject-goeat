package com.example.user.goeat_3.b_Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragments;

    public MainAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

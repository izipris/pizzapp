package com.pizzapp.ui.tabs;


import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pizzapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList;

    public TabAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.mFragmentList = new ArrayList<>();
        this.mFragmentTitleList = Arrays.asList(context.getString(
                R.string.tab_title_size),
                context.getString(R.string.tab_title_main),
                context.getString(R.string.tab_title_crust));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
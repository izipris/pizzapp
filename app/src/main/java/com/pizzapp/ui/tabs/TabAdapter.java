package com.pizzapp.ui.tabs;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pizzapp.MainActivity;
import com.pizzapp.ui.tabs.fragments.TabFragmentMain;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList;

    public TabAdapter(FragmentManager manager) {
        super(manager);
        this.mFragmentList = new ArrayList<>();
        this.mFragmentTitleList = new ArrayList<>();
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

    public void changePageTitle(int position, String title) {
        this.mFragmentTitleList.set(position, title);
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
        TabFragmentMain tabFragmentMain = (TabFragmentMain) mFragmentList.get(MainActivity.MAIN_FRAGMENT_INDEX);
        tabFragmentMain.showUpdatedPrice();
    }
}
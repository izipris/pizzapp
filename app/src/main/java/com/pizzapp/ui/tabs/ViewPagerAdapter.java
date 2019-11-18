package com.pizzapp.ui.tabs;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pizzapp.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String tabsTitles[];
    private Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
        this.tabsTitles = new String[]{context.getString(
                R.string.tab_title_size),
                context.getString(R.string.tab_title_mypizza),
                context.getString(R.string.tab_title_crust)};
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return tabsTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles[position];
    }
}
package com.pizzapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.ui.tabs.fragments.TabFragmentCrust;
import com.pizzapp.ui.tabs.fragments.TabFragmentMain;
import com.pizzapp.ui.tabs.fragments.TabFragmentSize;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);


        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new TabFragmentSize(), getString(R.string.tab_title_size));
        tabAdapter.addFragment(new TabFragmentMain(), getString(R.string.tab_title_main));
        tabAdapter.addFragment(new TabFragmentCrust(), getString(R.string.tab_title_crust));
        viewPager.setAdapter(tabAdapter);
        viewPager.setCurrentItem(1, false);
        tabLayout.setupWithViewPager(viewPager);
    }
}

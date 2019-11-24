package com.pizzapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.ui.tabs.fragments.TabFragmentCrust;
import com.pizzapp.ui.tabs.fragments.TabFragmentMain;
import com.pizzapp.ui.tabs.fragments.TabFragmentSize;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static final int MAIN_FRAGMENT_INDEX = 1;
    private Toolbar toolbar;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public Pizza pizza;
    public Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        extractExtras();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        initTabsLayout(viewPager, tabLayout);
    }

    private void extractExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pizza = (Pizza) extras.getSerializable("pizza");
            order = (Order) extras.getSerializable("order");
            viewPager = findViewById(R.id.viewpager);
            viewPager.setCurrentItem(MAIN_FRAGMENT_INDEX);

        }
    }

    private void initTabsLayout(ViewPager viewPager, TabLayout tabLayout) {
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new TabFragmentSize(tabAdapter, 0), getString(R.string.tab_title_size));
        tabAdapter.addFragment(new TabFragmentMain(), getString(R.string.tab_title_main));
        tabAdapter.addFragment(new TabFragmentCrust(tabAdapter, 2), getString(R.string.tab_title_crust));
        int viewPagerTabsLimit = (tabAdapter.getCount() > 1 ? tabAdapter.getCount() - 1 : 1);
        viewPager.setAdapter(tabAdapter);
        viewPager.setCurrentItem(MAIN_FRAGMENT_INDEX, false);
        viewPager.setOffscreenPageLimit(viewPagerTabsLimit);
        tabLayout.setupWithViewPager(viewPager);
    }

    public Pizza getPizza() {
        return pizza;
    }
}

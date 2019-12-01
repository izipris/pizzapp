package com.pizzapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.model.Database;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.ui.tabs.fragments.TabFragmentCrust;
import com.pizzapp.ui.tabs.fragments.TabFragmentMain;
import com.pizzapp.ui.tabs.fragments.TabFragmentSize;
import com.pizzapp.utilities.IO;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static final int MAIN_FRAGMENT_INDEX = 1;
    private Toolbar toolbar;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public Pizza pizza;
    public Order order;
    public Database database;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        extractExtras();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}

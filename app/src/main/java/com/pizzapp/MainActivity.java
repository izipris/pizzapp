package com.pizzapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.model.Database;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.ui.tabs.fragments.TabFragmentCrust;
import com.pizzapp.ui.tabs.fragments.TabFragmentMain;
import com.pizzapp.ui.tabs.fragments.TabFragmentSize;
import com.pizzapp.utilities.IO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    public static final int MAIN_FRAGMENT_INDEX = 1;
    private static final int DEFAULT_NUMBER_OF_SLICES = 4;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public Order order;
    public Database database;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));

        if (savedInstanceState != null) {
            order = (Order) savedInstanceState.getSerializable("order");
        } else {
            Pizza pizza = createDefaultPizza();
            order = new Order(0);
            order.addPizza(pizza);
        }

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        initTabsLayout(viewPager, tabLayout);
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
        initializeActivitiesCaptions();
    }

    private void initializeActivitiesCaptions() {
        final List<String> captions = Arrays.asList(
                getString(R.string.tab_caption_size),
                getString(R.string.tab_caption_main),
                getString(R.string.tab_caption_crust));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView greetingTextView = findViewById(R.id.textViewGreeting);
                greetingTextView.setText(captions.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public static void updatePizzaDimensionsIndicators(FragmentActivity activity, Pizza currentPizza) {
        TextView crustTextView = activity.findViewById(R.id.textViewChosenCrust);
        TextView sizeTextView = activity.findViewById(R.id.textViewChosenSize);
        crustTextView.setText(currentPizza.getCrust().getName());
        sizeTextView.setText(currentPizza.getSize().getCaption());
    }

    private Pizza createDefaultPizza() {
        Size defaultSize = database.getSizes().get(0);
        Crust defaultCrust = database.getCrusts().get(0);
        return new Pizza(DEFAULT_NUMBER_OF_SLICES, defaultSize, defaultCrust);
    }

    public Pizza getPizza() {
        return order.getLastPizza();
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("order", order);
    }
}

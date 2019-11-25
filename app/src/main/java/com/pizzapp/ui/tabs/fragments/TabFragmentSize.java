package com.pizzapp.ui.tabs.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.MainActivity;
import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.utilities.IO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TabFragmentSize extends Fragment {

    private TabAdapter tabAdapter;
    private int tabPosition;
    private Pizza currentPizza;
    static private final int DB_SIZE_M = 0;
    static private final int DB_SIZE_L = 1;
    static private final int DB_SIZE_XL = 2;
    private final int MAIN_TAB_INDEX = 1;

    public TabFragmentSize(TabAdapter tabAdapter, int tabPosition) {
        super();
        this.tabAdapter = tabAdapter;
        this.tabPosition = tabPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_size, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
        final ArrayList<Pair<ImageButton, Size>> buttonsAndSizes = setup(view, database);

        currentPizza = ((MainActivity) this.getActivity()).pizza;

        for (final Pair<ImageButton, Size> buttonSizePair : buttonsAndSizes) {
            buttonSizePair.first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    highlightChosenSize(buttonsAndSizes, buttonSizePair);
                }
            });
        }
    }

    private ArrayList<Pair<ImageButton, Size>> setup(@NonNull View view, Database database) {
        ArrayList<Pair<ImageButton, Size>> buttonsAndSizes = new ArrayList<>();
        Size currentSize;
        ImageButton currentButton = null;
        for (int i = 0; i < database.getSizes().size(); ++i) {
            currentSize = database.getSizes().get(i);
            switch (i) {
                case DB_SIZE_M:
                    ((TextView) view.findViewById(R.id.M)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.medium_pizza_price)).setText(
                            String.format(Locale.getDefault(), "%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol));
                    ((TextView) view.findViewById(R.id.medium_size)).setText(String.format(Locale.getDefault(), "%.1f",
                            currentSize.getDimension()) + getString(R.string.size_symbol));
                    currentButton = view.findViewById(R.id.medium_pizza_image);
                    break;
                case DB_SIZE_L:
                    ((TextView) view.findViewById(R.id.L)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.large_pizza_price)).setText(
                            String.format(Locale.getDefault(), "%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol));
                    ((TextView) view.findViewById(R.id.large_size)).setText(String.format(Locale.getDefault(), "%.1f",
                            currentSize.getDimension()) + getString(R.string.size_symbol));
                    currentButton = view.findViewById(R.id.large_pizza_image);
                    break;
                case DB_SIZE_XL:
                    ((TextView) view.findViewById(R.id.XL)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.extra_large_pizza_price)).setText(
                            String.format(Locale.getDefault(), "%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol));
                    ((TextView) view.findViewById(R.id.extra_large_size)).setText(String.format(Locale.getDefault(), "%.1f",
                            currentSize.getDimension()) + getString(R.string.size_symbol));
                    currentButton = view.findViewById(R.id.extra_large_pizza_image);
                    break;
            }
            buttonsAndSizes.add(new Pair<>(currentButton, currentSize));
        }
        return buttonsAndSizes;
    }

    private void highlightChosenSize(List<Pair<ImageButton, Size>> buttonsAndSizes, Pair<ImageButton, Size> chosen) {
        chosen.first.setBackgroundColor(getResources().getColor(R.color.colorChosenSizeBackground));
        for (final Pair<ImageButton, Size> buttonSizePair : buttonsAndSizes) {
            if (buttonSizePair.first != chosen.first) {
                buttonSizePair.first.setBackgroundResource(0);
            }
        }
        currentPizza.setSize(chosen.second);
        tabAdapter.changePageTitle(tabPosition, getString(R.string.tab_title_size) +
                getString(R.string.tab_title_separator) + chosen.second.getName());
        TabLayout tabs = getActivity().findViewById(R.id.tabs);
        tabs.getTabAt(MAIN_TAB_INDEX).select();
        tabAdapter.notifyDataSetChanged();
    }
}

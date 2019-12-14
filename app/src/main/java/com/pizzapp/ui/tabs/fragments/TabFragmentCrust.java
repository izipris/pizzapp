package com.pizzapp.ui.tabs.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.pizzapp.MainActivity;
import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.ui.tabs.TabAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TabFragmentCrust extends Fragment {

    private TabAdapter tabAdapter;
    private int tabPosition;
    private Pizza currentPizza;
    private boolean chooseAlready;
    private final int MAIN_TAB_INDEX = 1;
    private final int CRUST_CAPTION_TEXT_VIEW_POSITION = 0;

    public TabFragmentCrust(TabAdapter tabAdapter, int tabPosition) {
        super();
        this.tabAdapter = tabAdapter;
        this.tabPosition = tabPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_crust, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int DB_CRUST_THICK = 2;
        final int DB_CRUST_REGULAR = 1;
        final int DB_CRUST_THIN = 0;
        currentPizza = ((MainActivity) this.getActivity()).getPizza();
        chooseAlready = (savedInstanceState != null && savedInstanceState.getBoolean("chooseAlready")) || !((MainActivity) this.getActivity()).isNewOrder();
        TextView doughThickTextView = view.findViewById(R.id.doughThickTextView);
        TextView doughRegularTextView = view.findViewById(R.id.doughRegularTextView);
        TextView doughThinTextView = view.findViewById(R.id.doughThinTextView);
        LinearLayout doughThickLinearLayout = view.findViewById(R.id.layoutCrustThick);
        LinearLayout doughRegularLinearLayout = view.findViewById(R.id.layoutCrustRegular);
        LinearLayout doughThinLinearLayout = view.findViewById(R.id.layoutCrustThin);
        Database database = ((MainActivity) this.getActivity()).database;

        Crust thinCrust = database.getCrusts().get(DB_CRUST_THIN);
        Crust regularCrust = database.getCrusts().get(DB_CRUST_REGULAR);
        Crust thickCrust = database.getCrusts().get(DB_CRUST_THICK);

        Map<LinearLayout, Crust> buttonToTitleMap = generateButtonToCrustMapping(Arrays.asList(doughThickLinearLayout, doughRegularLinearLayout, doughThinLinearLayout),
                Arrays.asList(thickCrust, regularCrust, thinCrust));
        defineDoughButtonsHandlers(view, buttonToTitleMap);

        doughThickTextView.setText(getDoughTitle(thickCrust));
        doughRegularTextView.setText(getDoughTitle(regularCrust));
        doughThinTextView.setText(getDoughTitle(thinCrust));
    }

    private String getDoughTitle(Crust crust) {
        return crust.getPrice() > 0 ? crust.getName() + " + " +
                String.format(Locale.getDefault(), "%.2f", crust.getPrice()) +
                getString(R.string.currency_symbol) : crust.getName();
    }

    private void defineDoughButtonsHandlers(@NonNull View view, final Map<LinearLayout, Crust> layoutToCrustMapping) {
        // Define the onClick handlers such that when one option selected. it's highlighted
        // while the others are not.
        for (Map.Entry<LinearLayout, Crust> entryCurrent : layoutToCrustMapping.entrySet()) {
            final LinearLayout linearLayoutCurrent = entryCurrent.getKey();
            linearLayoutCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CountDownTimer(500, 1000) {
                        public void onTick(long millisUntilFinished) {
                            handleCrustClicked(layoutToCrustMapping, linearLayoutCurrent);
                            chooseAlready = true;
                        }

                        public void onFinish() {
                            TabLayout tabs = getActivity().findViewById(R.id.tabs);
                            tabs.getTabAt(MAIN_TAB_INDEX).select();
                        }
                    }.start();
                }
            });
            if (chooseAlready && entryCurrent.getValue().getName().equals(currentPizza.getCrust().getName())) {
                entryCurrent.getKey().setBackgroundResource(R.drawable.ic_dough_background);
                tabAdapter.changePageTitle(tabPosition, getString(R.string.tab_title_crust) +
                        getString(R.string.tab_title_separator) + layoutToCrustMapping.get(linearLayoutCurrent).getName());
                tabAdapter.notifyDataSetChanged();
            }

        }
    }

    private void handleCrustClicked(final Map<LinearLayout, Crust> layoutToCrustMapping, final LinearLayout linearLayoutCurrent) {
        linearLayoutCurrent.setBackgroundResource(R.drawable.ic_dough_background);
        TextView textViewCurrent = (TextView) linearLayoutCurrent.getChildAt(CRUST_CAPTION_TEXT_VIEW_POSITION);
        textViewCurrent.setTextColor(Color.WHITE);
        for (Map.Entry<LinearLayout, Crust> entryOther : layoutToCrustMapping.entrySet()) {
            LinearLayout linearLayoutOther = entryOther.getKey();
            if (linearLayoutOther != linearLayoutCurrent) {
                linearLayoutOther.setBackgroundResource(0);
                TextView textViewOther = (TextView) linearLayoutOther.getChildAt(CRUST_CAPTION_TEXT_VIEW_POSITION);
                textViewOther.setTextColor(Color.BLACK);
            }
        }
        ((MainActivity) this.getActivity()).getPizza().setCrust(layoutToCrustMapping.get(linearLayoutCurrent));
        tabAdapter.changePageTitle(tabPosition, getString(R.string.tab_title_crust) +
                getString(R.string.tab_title_separator) + layoutToCrustMapping.get(linearLayoutCurrent).getName());
        currentPizza.setCrust(layoutToCrustMapping.get(linearLayoutCurrent));
        MainActivity.updatePizzaDimensionsIndicators(getActivity(), currentPizza);
        tabAdapter.notifyDataSetChanged();
    }

    private Map<LinearLayout, Crust> generateButtonToCrustMapping(List<LinearLayout> linearLayoutList, List<Crust> crustList) {
        if (linearLayoutList.size() != crustList.size()) {
            return Collections.emptyMap();
        }
        Map<LinearLayout, Crust> map = new HashMap<>();
        for (int i = 0; i < linearLayoutList.size(); i++) {
            map.put(linearLayoutList.get(i), crustList.get(i));
        }
        return map;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("chooseAlready", chooseAlready);
    }
}

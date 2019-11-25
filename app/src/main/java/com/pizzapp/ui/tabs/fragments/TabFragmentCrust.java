package com.pizzapp.ui.tabs.fragments;

import android.os.Bundle;
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
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.utilities.IO;

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
    private final int MAIN_TAB_INDEX = 1;

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
        currentPizza = ((MainActivity) this.getActivity()).pizza;
        TextView doughThickTextView = view.findViewById(R.id.doughThickTextView);
        TextView doughRegularTextView = view.findViewById(R.id.doughRegularTextView);
        TextView doughThinTextView = view.findViewById(R.id.doughThinTextView);
        ImageButton doughThickImageButton = view.findViewById(R.id.doughThickImageButton);
        ImageButton doughRegularImageButton = view.findViewById(R.id.doughRegularImageButton);
        ImageButton doughThinImageButton = view.findViewById(R.id.doughThinImageButton);
        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));

        Crust thinCrust = database.getCrusts().get(DB_CRUST_THIN);
        Crust regularCrust = database.getCrusts().get(DB_CRUST_REGULAR);
        Crust thickCrust = database.getCrusts().get(DB_CRUST_THICK);

        Map<ImageButton, Crust> buttonToTitleMap = generateButtonToCrustMapping(Arrays.asList(doughThickImageButton, doughRegularImageButton, doughThinImageButton),
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

    private void defineDoughButtonsHandlers(@NonNull View view, final Map<ImageButton, Crust> buttonToCrustMapping) {
        // Define the onClick handlers such that when one option selected. it's highlighted
        // while the others are not.
        for (Map.Entry<ImageButton, Crust> entryCurrent : buttonToCrustMapping.entrySet()) {
            final ImageButton imageButtonCurrent = entryCurrent.getKey();
            imageButtonCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleCrustClicked(buttonToCrustMapping, imageButtonCurrent);
                }
            });
        }
    }

    private void handleCrustClicked(final Map<ImageButton, Crust> buttonToCrustMapping, final ImageButton imageButtonCurrent) {
        imageButtonCurrent.setBackgroundColor(getResources().getColor(R.color.colorChosenSizeBackground));
        for (Map.Entry<ImageButton, Crust> entryOther : buttonToCrustMapping.entrySet()) {
            ImageButton imageButtonOther = entryOther.getKey();
            if (imageButtonOther != imageButtonCurrent) {
                imageButtonOther.setBackgroundResource(0);
            }
        }
        currentPizza.setCrust(buttonToCrustMapping.get(imageButtonCurrent));
        tabAdapter.changePageTitle(tabPosition, getString(R.string.tab_title_crust) +
                getString(R.string.tab_title_separator) + buttonToCrustMapping.get(imageButtonCurrent).getName());
        TabLayout tabs = getActivity().findViewById(R.id.tabs);
        tabs.getTabAt(MAIN_TAB_INDEX).select();
        tabAdapter.notifyDataSetChanged();
    }

    private Map<ImageButton, Crust> generateButtonToCrustMapping(List<ImageButton> imageButtonList, List<Crust> crustList) {
        if (imageButtonList.size() != crustList.size()) {
            return Collections.emptyMap();
        }
        Map<ImageButton, Crust> map = new HashMap<>();
        for (int i = 0; i < imageButtonList.size(); i++) {
            map.put(imageButtonList.get(i), crustList.get(i));
        }
        return map;
    }
}

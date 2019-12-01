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

import com.pizzapp.MainActivity;
import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.ui.tabs.TabAdapter;
import com.pizzapp.utilities.IO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TabFragmentCrust extends Fragment {

    private TabAdapter tabAdapter;
    private int tabPosition;

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
        TextView doughThickTextView = view.findViewById(R.id.doughThickTextView);
        TextView doughRegularTextView = view.findViewById(R.id.doughRegularTextView);
        TextView doughThinTextView = view.findViewById(R.id.doughThinTextView);
        ImageButton doughThickImageButton = view.findViewById(R.id.doughThickImageButton);
        ImageButton doughRegularImageButton = view.findViewById(R.id.doughRegularImageButton);
        ImageButton doughThinImageButton = view.findViewById(R.id.doughThinImageButton);
        Database database = ((MainActivity) this.getActivity()).database;

        Crust thinCrust = database.getCrusts().get(DB_CRUST_THIN);
        Crust regularCrust = database.getCrusts().get(DB_CRUST_REGULAR);
        Crust thickCrust = database.getCrusts().get(DB_CRUST_THICK);

        Map<ImageButton, String> buttonToTitleMap = generateButtonToTitleMapping(Arrays.asList(doughThickImageButton, doughRegularImageButton, doughThinImageButton),
                Arrays.asList(thickCrust.getName(), regularCrust.getName(), thinCrust.getName()));
        defineDoughButtonsHandlers(buttonToTitleMap);

        doughThickTextView.setText(getDoughTitle(thickCrust));
        doughRegularTextView.setText(getDoughTitle(regularCrust));
        doughThinTextView.setText(getDoughTitle(thinCrust));
    }

    private String getDoughTitle(Crust crust) {
        return crust.getPrice() > 0 ? crust.getName() + " + " +
                String.format(Locale.getDefault(), "%.2f", crust.getPrice()) +
                getString(R.string.currency_symbol) : crust.getName();
    }

    private void defineDoughButtonsHandlers(final Map<ImageButton, String> buttonToTextMapping) {
        // Define the onClick handlers such that when one option selected. it's highlighted
        // while the others are not.
        for (Map.Entry<ImageButton, String> entryCurrent : buttonToTextMapping.entrySet()) {
            final ImageButton imageButtonCurrent = entryCurrent.getKey();
            imageButtonCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleCrustClicked(buttonToTextMapping, imageButtonCurrent);
                }
            });
        }
    }

    private void handleCrustClicked(final Map<ImageButton, String> buttonToTextMapping, final ImageButton imageButtonCurrent) {
        imageButtonCurrent.setBackgroundColor(getResources().getColor(R.color.colorChosenSizeBackground));
        for (Map.Entry<ImageButton, String> entryOther : buttonToTextMapping.entrySet()) {
            ImageButton imageButtonOther = entryOther.getKey();
            if (imageButtonOther != imageButtonCurrent) {
                imageButtonOther.setBackgroundResource(0);
            }
        }
        tabAdapter.changePageTitle(tabPosition, getString(R.string.tab_title_crust) +
                getString(R.string.tab_title_separator) + buttonToTextMapping.get(imageButtonCurrent));
        tabAdapter.notifyDataSetChanged();
    }

    private Map<ImageButton, String> generateButtonToTitleMapping(List<ImageButton> imageButtonList, List<String> textList) {
        if (imageButtonList.size() != textList.size()) {
            return Collections.emptyMap();
        }
        Map<ImageButton, String> map = new HashMap<>();
        for (int i = 0; i < imageButtonList.size(); i++) {
            map.put(imageButtonList.get(i), textList.get(i));
        }
        return map;
    }
}

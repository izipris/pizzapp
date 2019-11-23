package com.pizzapp.ui.tabs.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.utilities.IO;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabFragmentCrust extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_crust, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
        Map<ImageButton, TextView> buttonToTitleMap = generateButtonToTitleMapping(Arrays.asList(doughThickImageButton, doughRegularImageButton, doughThinImageButton),
                Arrays.asList(doughThickTextView, doughRegularTextView, doughThinTextView));
        defineDoughButtonsHandlers(buttonToTitleMap);
        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));

        Crust thinCrust = database.getCrusts().get(DB_CRUST_THIN);
        Crust regularCrust = database.getCrusts().get(DB_CRUST_REGULAR);
        Crust thickCrust = database.getCrusts().get(DB_CRUST_THICK);

        doughThickTextView.setText(getDoughTitle(thickCrust));
        doughRegularTextView.setText(getDoughTitle(regularCrust));
        doughThinTextView.setText(getDoughTitle(thinCrust));
    }

    private String getDoughTitle(Crust crust) {
        return crust.getPrice() > 0 ? crust.getName() + " + " +
                String.format("%.2f", crust.getPrice()) +
                getString(R.string.currency_symbol) : crust.getName();
    }

    private void defineDoughButtonsHandlers(final Map<ImageButton, TextView> buttonToTextMapping) {
        // Define the onClick handlers such that when one option selected. it's highlighted
        // while the others are not.
        for (Map.Entry<ImageButton, TextView> entryCurrent : buttonToTextMapping.entrySet()) {
            final ImageButton imageButtonCurrent = entryCurrent.getKey();
            final TextView textViewCurrent = entryCurrent.getValue();
            imageButtonCurrent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textViewCurrent.setTextColor(Color.RED);
                    for (Map.Entry<ImageButton, TextView> entryOther : buttonToTextMapping.entrySet()) {
                        TextView textVieOther = entryOther.getValue();
                        if (textViewCurrent != textVieOther) {
                            textVieOther.setTextColor(Color.BLACK);
                        }
                    }
                }
            });
        }
    }

    private Map<ImageButton, TextView> generateButtonToTitleMapping(List<ImageButton> imageButtonList, List<TextView> textViewList) {
        if (imageButtonList.size() != textViewList.size()) {
            return Collections.emptyMap();
        }
        Map<ImageButton, TextView> map = new HashMap<>();
        for (int i = 0; i < imageButtonList.size(); i++) {
            map.put(imageButtonList.get(i), textViewList.get(i));
        }
        return map;
    }
}

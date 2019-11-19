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

import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.utilities.IO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TabFragmentSize extends Fragment {

    private Size chosenSize;
    static private final int DB_SIZE_M = 0;
    static private final int DB_SIZE_L = 1;
    static private final int DB_SIZE_XL = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_size, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
        setupText(view, database);

        chosenSize = database.getSizes().get(DB_SIZE_M);

        ImageButton mediumPizzaButton = view.findViewById(R.id.medium_pizza_image);
        ImageButton largePizzaButton = view.findViewById(R.id.large_pizza_image);
        ImageButton extraLargePizzaButton = view.findViewById(R.id.extra_large_pizza_image);
        final ArrayList<ImageButton> buttons = new ArrayList<>(Arrays.asList(mediumPizzaButton, largePizzaButton, extraLargePizzaButton));
        for(final ImageButton button: buttons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    highlightChosenSize(buttons, button);
                }
            });
        }
    }

    private void setupText(@NonNull View view, Database database){
        Size currentSize;
        for(int i = 0; i < database.getSizes().size(); ++i)
        {
            currentSize = database.getSizes().get(i);
            switch (i){
                case DB_SIZE_M:
                    ((TextView) view.findViewById(R.id.M)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.medium_pizza_price)).setText(
                            String.format(Locale.getDefault(),"%.2f", currentSize.getPrice()) +
                            getString(R.string.currency_symbol) );
                    break;
                case DB_SIZE_L:
                    ((TextView) view.findViewById(R.id.L)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.large_pizza_price)).setText(
                            String.format(Locale.getDefault(),"%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol) );
                    break;
                case DB_SIZE_XL:
                    ((TextView) view.findViewById(R.id.XL)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.extra_large_pizza_price)).setText(
                            String.format(Locale.getDefault(),"%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol) );
                    break;
            }
        }

    }

    private void highlightChosenSize(List<ImageButton> buttons, ImageButton chosen){
        chosen.setBackgroundColor(getResources().getColor(R.color.colorChosenSizeBackground));
        for(ImageButton button:buttons){
            if(button != chosen) {
                button.setBackgroundResource(0);
            }
        }
    }

}

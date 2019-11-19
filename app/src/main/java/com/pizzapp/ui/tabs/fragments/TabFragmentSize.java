package com.pizzapp.ui.tabs.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

public class TabFragmentSize extends Fragment {

    private Size chosenSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_size, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));

        chosenSize = database.getSizes().get(0);

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

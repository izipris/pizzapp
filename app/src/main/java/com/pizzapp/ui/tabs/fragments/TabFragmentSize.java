package com.pizzapp.ui.tabs.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pizzapp.MainActivity;
import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.utilities.IO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TabFragmentSize extends Fragment  {

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
        final ArrayList<Pair<ImageButton, Size>> buttonsAndSizes = setup(view, database);

        if (((MainActivity)this.getActivity()).pizza == null){
            chosenSize = database.getSizes().get(DB_SIZE_M); /* need to fix */
        } else {
            chosenSize = ((MainActivity) this.getActivity()).pizza.getSize();
        }
        chosenSize = database.getSizes().get(DB_SIZE_M);

        for(final Pair<ImageButton, Size> buttonSizePair: buttonsAndSizes){
            buttonSizePair.first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    highlightChosenSize(buttonsAndSizes, buttonSizePair);
                    displayToast(view, getResources().getString(R.string.message_for_size_choosing) + " " +
                            buttonSizePair.second.getName());
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
                    currentButton = view.findViewById(R.id.medium_pizza_image);
                    break;
                case DB_SIZE_L:
                    ((TextView) view.findViewById(R.id.L)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.large_pizza_price)).setText(
                            String.format(Locale.getDefault(), "%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol));
                    currentButton = view.findViewById(R.id.large_pizza_image);
                    break;
                case DB_SIZE_XL:
                    ((TextView) view.findViewById(R.id.XL)).setText(currentSize.getName());
                    ((TextView) view.findViewById(R.id.extra_large_pizza_price)).setText(
                            String.format(Locale.getDefault(), "%.2f", currentSize.getPrice()) +
                                    getString(R.string.currency_symbol));
                    currentButton = view.findViewById(R.id.extra_large_pizza_image);
                    break;
            }
            buttonsAndSizes.add(new Pair<>(currentButton, currentSize));
        }
        return buttonsAndSizes;
    }

    private void highlightChosenSize(List<Pair<ImageButton, Size>> buttonsAndSizes, Pair<ImageButton, Size> chosen) {
        chosen.first.setBackgroundColor(getResources().getColor(R.color.colorChosenSizeBackground));
        for (final Pair<ImageButton, Size> buttonSizePair: buttonsAndSizes) {
            if (buttonSizePair.first != chosen.first) {
                buttonSizePair.first.setBackgroundResource(0);
            }
        }
        chosenSize = chosen.second;
    }

    public void displayToast(@NonNull View view, String message) {
        Toast.makeText(view.getContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public Size getChosenSize() {
        return chosenSize;
    }
}

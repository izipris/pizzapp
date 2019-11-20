package com.pizzapp.ui.tabs.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pizzapp.R;
import com.pizzapp.ToppingPopUp;
import com.pizzapp.ToppingsPopUp;
import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Slice;
import com.pizzapp.utilities.IO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabFragmentMain extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView topRightSlice = view.findViewById(R.id.topRight);
        ImageView bottomRightSlice = view.findViewById(R.id.bottomRight);
        ImageView bottomLeftSlice = view.findViewById(R.id.bottomLeft);
        ImageView topLeftSlice = view.findViewById(R.id.topLeft);
        List<ImageView> slices = Arrays.asList(topRightSlice, bottomRightSlice, bottomLeftSlice, topLeftSlice);
        addOnClickListener(slices);
//        Map<ImageButton, TextView> buttonToTitleMap = generateButtonToTitleMapping(Arrays.asList(doughThickImageButton, doughRegularImageButton, doughThinImageButton),
//                Arrays.asList(doughThickTextView, doughRegularTextView, doughThinTextView));
//        defineDoughButtonsHandlers(buttonToTitleMap);
//        Database database = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
//
//        Crust thinCrust = database.getCrusts().get(DB_CRUST_THIN);
//        Crust regularCrust = database.getCrusts().get(DB_CRUST_REGULAR);
//        Crust thickCrust = database.getCrusts().get(DB_CRUST_THICK);
//
//        doughThickTextView.setText(getDoughTitle(thickCrust));
//        doughRegularTextView.setText(getDoughTitle(regularCrust));
//        doughThinTextView.setText(getDoughTitle(thinCrust));

    }

    private void addOnClickListener(List<ImageView> slices) {
        for (ImageView slice: slices){
            slice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ToppingsPopUp.class);
                    startActivity(intent);
                    }
            });
            }
        }
}

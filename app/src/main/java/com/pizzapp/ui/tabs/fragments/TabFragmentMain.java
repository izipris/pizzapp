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
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.Slice;
import com.pizzapp.utilities.IO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class TabFragmentMain extends Fragment implements Serializable {

    private static final int POPUP_ACTIVITY_REQUEST_CODE = 0;
    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int PIZZA_PASSED = 3;
    private static final int PIZZA_NOT_PASSED = 2;

    Pizza currentPizza;

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
    }


    private void addOnClickListener(List<ImageView> slices) {
        for (ImageView slice: slices){
            slice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ToppingsPopUp.class);
                    int numberOfExtrasPassed;
                    intent.putExtra("callingId", getPartClicked(v.getId()));
                    if (currentPizza != null) {
                        numberOfExtrasPassed = PIZZA_PASSED;
                        intent.putExtra("pizza", currentPizza);
                    } else {
                        numberOfExtrasPassed = PIZZA_NOT_PASSED;
                    }
                    intent.putExtra("numberOfExtras", numberOfExtrasPassed);
                    startActivityForResult(intent, POPUP_ACTIVITY_REQUEST_CODE);
                    }
            });
        }
    }

    private int getPartClicked(int id){
        switch (id){
            case (R.id.topRight):
                return TOP_RIGHT_SLICE;
            case (R.id.bottomRight):
                return BOTTOM_RIGHT_SLICE;
            case (R.id.bottomLeft):
                return BOTTOM_LEFT_SLICE;
            case (R.id.topLeft):
                return TOP_LEFT_SLICE;
        }
        return -1;
    }

}

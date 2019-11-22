package com.pizzapp.ui.tabs.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pizzapp.MainActivity;
import com.pizzapp.R;
import com.pizzapp.ToppingsPopUp;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class TabFragmentMain extends Fragment implements Serializable {

    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int PIZZA_PASSED = 3;
    private static final int PIZZA_NOT_PASSED = 2;
    private static final int HEIGHT = 76;
    private static final int WIDTH = 76;
    private static final int ANGLE_TO_ROTATE = 90;

    Pizza currentPizza;
    Order finalOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPizza = ((MainActivity)this.getActivity()).pizza;
        ImageView topRightSlice = view.findViewById(R.id.topRight);
        ImageView bottomRightSlice = view.findViewById(R.id.bottomRight);
        ImageView bottomLeftSlice = view.findViewById(R.id.bottomLeft);
        ImageView topLeftSlice = view.findViewById(R.id.topLeft);
        List<ImageView> slices = Arrays.asList(topRightSlice, bottomRightSlice, bottomLeftSlice, topLeftSlice);
        addOnClickListener(slices);
        createPizza(view);
    }


    private void createPizza(View view) {
        int currentPart = 0;
        if (currentPizza != null) {
            for (PizzaPart pizzaPart : currentPizza.getParts()) {
                for (Topping topping : pizzaPart.getToppings()) {
                    addTopping(view, currentPart, topping);
                }
                currentPart++;
            }
        }
    }

    private void addTopping(View view, final int currentPart, Topping topping) {
        FrameLayout frameLayout = getAppropriateFrameId(currentPart, view);
        ImageView newTopping = new ImageView(getActivity());
        newTopping.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
        newTopping.setRotation(getCurrentRotation(currentPart));
        newTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup(currentPart);            }
        });
        FrameLayout.LayoutParams layoutParams = new
                FrameLayout.LayoutParams(convertDpToPx(154),convertDpToPx(154));
        setGravity(layoutParams, currentPart);
        layoutParams.height = convertDpToPx(HEIGHT);
        layoutParams.width = convertDpToPx(WIDTH);

        newTopping.setLayoutParams(layoutParams);
        frameLayout.addView(newTopping);
    }

    private FrameLayout getAppropriateFrameId(int sliceId, View view) {
            switch (sliceId){
                case (TOP_RIGHT_SLICE):
                    return view.findViewById(R.id.topRightFrame);
                case (BOTTOM_RIGHT_SLICE):
                    return view.findViewById(R.id.bottomRightFrame);
                case (BOTTOM_LEFT_SLICE):
                    return view.findViewById(R.id.bottomLeftFrame);
            }
            return view.findViewById(R.id.topLeftFrame);

    }

    private void setGravity(FrameLayout.LayoutParams layoutParams, int currentPart){
        switch (currentPart){
            case (TOP_RIGHT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            case (BOTTOM_RIGHT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            case (BOTTOM_LEFT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;
            case (TOP_LEFT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        }
    }

    private int getCurrentRotation(int currentPart){
        return ANGLE_TO_ROTATE * currentPart;
    }

    private Drawable convertStringToDrawable(String name){
        int id = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
        return getResources().getDrawable(id);
    }

    private int convertDpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void addOnClickListener(List<ImageView> slices) {
        for (ImageView slice: slices){
            slice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPopup(v.getId());
                    }
            });
        }
    }

    private void openPopup(int id) {
        Intent intent = new Intent(getActivity(), ToppingsPopUp.class);
        int numberOfExtrasPassed;
        intent.putExtra("callingId", getPartClicked(id));
        if (currentPizza != null) {
            numberOfExtrasPassed = PIZZA_PASSED;
            intent.putExtra("pizza", currentPizza);
        } else {
            numberOfExtrasPassed = PIZZA_NOT_PASSED;
        }
        intent.putExtra("numberOfExtras", numberOfExtrasPassed);
        startActivity(intent);
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


    public void addPizza(View view) {
    }

    public void clearPizza(View view) {
        for (PizzaPart pizzaPart:currentPizza.getParts()){

        }
    }

    public void placeOrder(View view) {
    }
}

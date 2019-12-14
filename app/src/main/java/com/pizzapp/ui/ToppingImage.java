package com.pizzapp.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pizzapp.utilities.StaticFunctions;

class ToppingImage extends Image {

    private static final double TOPPING_ORIGINAL_HEIGHT = 107.5;
    private static final double TOPPING_ORIGINAL_WIDTH = 107.5;
    private static final int TOPPING_ENLARGED_WIDTH = 123;
    private static final int TOPPING_ENLARGED_HEIGHT = 123;

    ToppingImage(int pizzaPart, int id, String name, View view) {
        this.pizzaPart = pizzaPart;
        this.id = id;
        this.name = name;
        this.view = view;
    }

    void shrinkImage() {
        ImageView image = view.findViewById(id);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_HEIGHT);
        layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_WIDTH);
        image.setLayoutParams(layoutParams);
    }

    void enlargeImage() {
        ImageView image = view.findViewById(id);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_HEIGHT);
        layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_WIDTH);
        image.setLayoutParams(layoutParams);
    }

}

package com.pizzapp.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.StaticFunctions;

import java.util.ArrayList;
import java.util.List;

public class PizzaPartImage extends Image {

    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int ANGLE_TO_ROTATE = 90;
    private static final double TOPPING_ORIGINAL_HEIGHT = 107.5;
    private static final double TOPPING_ORIGINAL_WIDTH = 107.5;
    private static final int TOPPING_ENLARGED_WIDTH = 123;
    private static final int TOPPING_ENLARGED_HEIGHT = 123;
    private final int ENLARGED_WIDTH = 143;
    private final int ENLARGED_HEIGHT = 143;
    private static final double ORIGINAL_WIDTH = 127.5;
    private static final double ORIGINAL_HEIGHT = 127.5;

    private List<ToppingImage> toppingImageList = new ArrayList<>();
    private int frameId;
    private int plusId;

    public PizzaPartImage(int frameId, int plusId, int pizzaPart, int id, String name, View view){
        this.frameId = frameId;
        this.plusId = plusId;
        this.pizzaPart = pizzaPart;
        this.id = id;
        this.name = name;
        this.view = view;
    }

    public void shrinkSlice() {
        for (ToppingImage toppingImage : toppingImageList) {
            toppingImage.shrinkImage();
        }
        shrinkImage();
        addPlus();
    }

    private void addPlus() {
        view.findViewById(plusId).setVisibility(View.VISIBLE);
    }

    private void shrinkImage() {
        ImageView image = view.findViewById(id);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = StaticFunctions.convertDpToPx(ORIGINAL_HEIGHT);
        layoutParams.width = StaticFunctions.convertDpToPx(ORIGINAL_WIDTH);
        image.setLayoutParams(layoutParams);
    }

    public void enlargeSlice() {
        for (ToppingImage toppingImage : toppingImageList) {
            toppingImage.enlargeImage();
        }
        enlargeImage();
        removePlus();
    }

    private void enlargeImage() {
        ImageView image = view.findViewById(id);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        layoutParams.height = StaticFunctions.convertDpToPx(ENLARGED_HEIGHT);
        layoutParams.width = StaticFunctions.convertDpToPx(ENLARGED_WIDTH);
        image.setLayoutParams(layoutParams);
    }

    private void removePlus() {
        view.findViewById(plusId).setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addTopping(Topping topping, boolean initiation) {
        int toppingId = StaticFunctions.generateRandomNumber();
        toppingImageList.add(new ToppingImage(pizzaPart, toppingId, topping.getName(), view));
        addToppingToPizza(topping, toppingId, initiation);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingToPizza(Topping topping, int toppingId, boolean initiationOfActivity) {
        FrameLayout frameLayout = view.findViewById(frameId);
        ImageView newTopping = new ImageView(view.getContext());
        newTopping.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
        newTopping.setRotation(getCurrentRotation(pizzaPart));
        newTopping.setId(toppingId);
        FrameLayout.LayoutParams layoutParams = new
                FrameLayout.LayoutParams(StaticFunctions.convertDpToPx(ENLARGED_HEIGHT),
                StaticFunctions.convertDpToPx(ENLARGED_WIDTH));
        setGravity(layoutParams, pizzaPart);
        if (initiationOfActivity) {
            layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_HEIGHT);
            layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_WIDTH);
        } else {
            layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_HEIGHT);
            layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_WIDTH);
        }
        newTopping.setLayoutParams(layoutParams);
        frameLayout.addView(newTopping);
        view.findViewById(plusId).bringToFront();
    }

    private Drawable convertStringToDrawable(String name) {
        int id = view.getResources().getIdentifier(name, "drawable",
                view.getContext().getPackageName());
        return view.getResources().getDrawable(id);
    }

    private int getCurrentRotation(int pizzaPart) {
        return ANGLE_TO_ROTATE * pizzaPart;
    }

    private void setGravity(FrameLayout.LayoutParams layoutParams, int pizzaPart) {
        switch (pizzaPart) {
            case (TOP_RIGHT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                break;
            case (BOTTOM_RIGHT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.START;
                break;
            case (BOTTOM_LEFT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.END;
                break;
            case (TOP_LEFT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                break;
        }
    }

    public void removeTopping(Topping topping) {
        ToppingImage toppingToRemove = getToppingId(topping);
        ImageView toppingToRemoveImage = view.findViewById(toppingToRemove.id);
        toppingToRemoveImage.setVisibility(View.GONE);
        toppingImageList.remove(toppingToRemove);
    }

    public void removeAllTopping(){
        List<ToppingImage> tempImageList = new ArrayList<>(toppingImageList);
        for (ToppingImage toppingToRemove: tempImageList){
            ImageView toppingToRemoveImage = view.findViewById(toppingToRemove.id);
            toppingToRemoveImage.setVisibility(View.GONE);
            toppingImageList.remove(toppingToRemove);
        }
    }

    private ToppingImage getToppingId(Topping topping) {
        for (ToppingImage toppingImage: toppingImageList){
            if (toppingImage.getName().equals(topping.getName())){
                return toppingImage;
            }
        }
        return null;
    }

    public boolean hasTopping(Topping topping){
        for (ToppingImage toppingImage: toppingImageList){
            if (toppingImage.getName().equals(topping.getName())){
                return true;
            }
        }
        return false;
    }
}


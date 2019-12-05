package com.pizzapp.utilities.UI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pizzapp.utilities.StaticFunctions;

import java.util.List;

public class Image {
    protected int id;
    protected int pizzaPart;
    protected String name;
    protected View view;
    protected static List<Integer> imageIds;

    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int ANGLE_TO_ROTATE = 90;
    private final String TOPPING_PICKED_INDICATOR = "@drawable/clicked_button_1";
    private final String SPACE_KEEPER = "@drawable/rectangle_spot";

    public Image(){
        id = -1;
        pizzaPart = -1;
        name = "";
        this.view = null;
    }

    private void shrinkImage(View view, int imageId, boolean isTopping) {
        ImageView image = view.findViewById(imageId);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        double width, height;
        if (isTopping) {
            height = TOPPING_ORIGINAL_HEIGHT;
            width = TOPPING_ORIGINAL_WIDTH;
        } else {
            height = ORIGINAL_HEIGHT;
            width = ORIGINAL_WIDTH;
        }
        layoutParams.height = StaticFunctions.convertDpToPx(height);
        layoutParams.width = StaticFunctions.convertDpToPx(width);
        image.setLayoutParams(layoutParams);
    }

    public void enlargeImage(View view, Integer sliceId, boolean isTopping) {
        ImageView image = view.findViewById(sliceId);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        int height;
        int width;
        if (isTopping) {
            height = TOPPING_ENLARGED_HEIGHT;
            width = TOPPING_ENLARGED_WIDTH;
        } else {
            height = ENLARGED_HEIGHT;
            width = ENLARGED_WIDTH;
        }
        layoutParams.height = StaticFunctions.convertDpToPx(height);
        layoutParams.width = StaticFunctions.convertDpToPx(width);
        image.setLayoutParams(layoutParams);
    }

    public int getId(){
        return id;
    }

    public int getPizzaPart(){
        return pizzaPart;
    }

    public String getName(){
        return name;
    }

    public List<Integer> getImageIds(){
        return imageIds;
    }

    public void addId(int id){
        imageIds.add(id);
    }

    public void removeId(int id){
        imageIds.remove(id);
    }

}

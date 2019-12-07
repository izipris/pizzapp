package com.pizzapp.utilities.UI;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.StaticFunctions;

public class ToppingBox {

    private final String TOPPING_PICKED_INDICATOR = "@drawable/topping_on_pizza_indicator";
    private final String BUTTON_SQUARE = "@drawable/plain_square_to_indicate_button";
    LinearLayout toppingBox;
    private int boxId;
    private Topping topping;
    private View view;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    ToppingBox(Topping topping, View view, int row, int col, int numberOfRows){
        this.boxId = numberOfRows * col + row;
        this.topping = topping;
        this.view = view;
        createToppingBox();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createToppingBox() {
        toppingBox = new LinearLayout(view.getContext());
        toppingBox.setPadding(StaticFunctions.convertDpToPx(15), StaticFunctions.convertDpToPx(8),
                StaticFunctions.convertDpToPx(15), StaticFunctions.convertDpToPx(8));
        toppingBox.setId(boxId);
        toppingBox.setOrientation(LinearLayout.HORIZONTAL);
        addToppingDataToToppingBox(toppingBox, topping);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingDataToToppingBox(final LinearLayout toppingBox, final Topping topping) {
        ImageView iconTopping = new ImageView(view.getContext());
        iconTopping.setImageDrawable(convertStringToDrawable(BUTTON_SQUARE));
        iconTopping.setPadding(5, 0, StaticFunctions.convertDpToPx(10), 0);
        TextView iconText = new TextView(view.getContext());
        String dots = calculateNumberOfDotFillersRequired(topping);
        String textToDisplay = topping.getName() + "  " + dots + "  ";
        iconText.setText(textToDisplay);
        TextView iconPrice = new TextView(view.getContext());
        iconPrice.setText(String.valueOf(topping.getPrice() / 4));
        toppingBox.addView(iconTopping);
        toppingBox.addView(iconText);
        toppingBox.addView(iconPrice);
    }

    private String calculateNumberOfDotFillersRequired(Topping topping) {
        int lengthOfTopping = topping.getName().length();
        StringBuilder buf = new StringBuilder(28 - 2 * lengthOfTopping);
        for (int i = 0; i < 24 - 2 * lengthOfTopping; i++) {
            buf.append('.');
        }
        return buf.toString();

    }

    private Drawable convertStringToDrawable(String name) {
        int id = view.getResources().getIdentifier(name,
                "drawable", view.getContext().getPackageName());
        return view.getResources().getDrawable(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void removeToppingOnPizzaIndicatorFromToppingBox() {
        toppingBox.getChildAt(0).setVisibility(View.VISIBLE);
        toppingBox.setBackground(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addToppingOnPizzaIndicatorToToppingBox() {
        toppingBox.getChildAt(0).setVisibility(View.INVISIBLE);
        toppingBox.setBackground(convertStringToDrawable(TOPPING_PICKED_INDICATOR));
    }

    public int getId(){
        return boxId;
    }

    public Topping getTopping(){
        return topping;
    }

    public LinearLayout getToppingBox(){
        return toppingBox;
    }
}

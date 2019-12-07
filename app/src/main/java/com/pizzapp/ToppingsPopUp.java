package com.pizzapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.IO;
import com.pizzapp.utilities.StaticFunctions;
import com.pizzapp.utilities.UI.PizzaPartImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class ToppingsPopUp extends AppCompatActivity implements Serializable {

    private static final String LOG_TAG = ToppingsPopUp.class.getSimpleName();

    private final String TOPPING_PICKED_INDICATOR = "@drawable/topping_on_pizza_indicator";
    private final String BUTTON_SQUARE = "@drawable/plain_sqaure_to_indicate_button";

    private static final int MIN_ROWS_IN_CHART = 4;

    private int currentSliceIdOutOfFour;
    private Pizza pizza;
    private List<PizzaPartImage> partImages = new ArrayList<>();
    private List<PizzaPartImage> partImagesEnlarged = new ArrayList<>();
    private List<Topping> toppingsList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_toppings);
        initializePizzaPartImageList(findViewById(android.R.id.content).getRootView());
        extractExtras();
        toppingsList = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database)).getToppings();
        createToppingChart();
    }

    private void initializePizzaPartImageList(View view) {
        partImages.add(new PizzaPartImage(R.id.topRightFrame, R.id.pizzaPLusTopRight, 0, R.id.topRight, "topRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomRightFrame, R.id.pizzaPLusBottomRight, 1, R.id.bottomRight, "bottomRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomLeftFrame, R.id.pizzaPLusBottomleft,  2, R.id.bottomLeft, "bottomLeft", view));
        partImages.add(new PizzaPartImage(R.id.topLeftFrame, R.id.pizzaPLusTopleft, 3, R.id.topLeft, "topLeft", view));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void extractExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentSliceIdOutOfFour = extras.getInt("callingId");
            pizza = (Pizza) extras.getSerializable("pizza");
            createInitialPizza();
            partImagesEnlarged.add(partImages.get(currentSliceIdOutOfFour));
            partImages.get(currentSliceIdOutOfFour).enlargeSlice();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createInitialPizza() {
        for (int i = 0; i < pizza.getNumberOfParts(); i++) {
            for (Topping topping : pizza.getParts().get(i).getToppings()) {
                partImages.get(i).addTopping(topping, true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createToppingChart() {
        GridLayout layout = findViewById(R.id.grid_layout);
        layout.setUseDefaultMargins(true);
        int numberOfRows = min(MIN_ROWS_IN_CHART, toppingsList.size());
        layout.setRowCount(numberOfRows);
        int numberOfColumns = calculateNumberOfColumns();
        layout.setColumnCount(numberOfColumns);
        for (int i = 0; i < numberOfRows; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1, 0);
            for (int j = 0; j < numberOfColumns; j++) {
                if (j * MIN_ROWS_IN_CHART + i == toppingsList.size()) {
                    break;
                }
                GridLayout.Spec colSpec = GridLayout.spec(j, 1, 1);
                LinearLayout toppingBox = createToppingBox(i, j);
                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
                myGLP.rowSpec = rowSpec;
                myGLP.columnSpec = colSpec;
                layout.addView(toppingBox, myGLP);
            }
        }
    }

    private int calculateNumberOfColumns() {
        int numberOfRows = toppingsList.size() / MIN_ROWS_IN_CHART;
        if (toppingsList.size() % MIN_ROWS_IN_CHART != 0) {
            numberOfRows++;
        }
        return numberOfRows;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private LinearLayout createToppingBox(int row, int col) {
        final LinearLayout toppingBox = new LinearLayout(this);
        final Topping topping = toppingsList.get(MIN_ROWS_IN_CHART * col + row);
        toppingBox.setPadding(StaticFunctions.convertDpToPx(15), StaticFunctions.convertDpToPx(8),
                StaticFunctions.convertDpToPx(15), StaticFunctions.convertDpToPx(8));
        toppingBox.setId(MIN_ROWS_IN_CHART * col + row);
        toppingBox.setOrientation(LinearLayout.HORIZONTAL);
        addToppingDataToToppingBox(toppingBox, topping);
        if (pizza.getPizzaPart(currentSliceIdOutOfFour).hasCertainTopping(topping)) {
            addToppingOnPizzaIndicatorToToppingBox(toppingBox);
        }
        toppingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveTopping(toppingBox, topping);
            }
        });
        return toppingBox;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addOrRemoveTopping(LinearLayout toppingBox, Topping topping) {
        if (toppingBox.getChildAt(0).getVisibility() == View.INVISIBLE) {
            removeTopping(topping);
            removeToppingOnPizzaIndicatorFromToppingBox(toppingBox);
        } else {
            for (PizzaPartImage pizzaPartImage : partImagesEnlarged) {
                if (!pizzaPartImage.hasTopping(topping)) {
                    addTopping(topping);
                    addToppingOnPizzaIndicatorToToppingBox(toppingBox);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingOnPizzaIndicatorToToppingBox(LinearLayout toppingBox) {
        toppingBox.getChildAt(0).setVisibility(View.INVISIBLE);
        toppingBox.setBackground(convertStringToDrawable(TOPPING_PICKED_INDICATOR));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void removeToppingOnPizzaIndicatorFromToppingBox(LinearLayout toppingBox) {
        toppingBox.getChildAt(0).setVisibility(View.VISIBLE);
        toppingBox.setBackground(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingDataToToppingBox(final LinearLayout toppingBox, final Topping topping) {
        ImageView iconTopping = new ImageView(this);
        iconTopping.setImageDrawable(convertStringToDrawable(BUTTON_SQUARE));
        iconTopping.setPadding(5, 0, StaticFunctions.convertDpToPx(10), 0);
        iconTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveTopping(toppingBox, topping);
            }
        });
        TextView iconText = new TextView(this);
        String dots = calculateNumberOfDotFillersRequired(topping);
        String textToDisplay = topping.getName() + "  " + dots + "  ";
        iconText.setText(textToDisplay);
        iconText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemoveTopping(toppingBox, topping);
            }
        });
        TextView iconPrice = new TextView(this);
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
        int id = getResources().getIdentifier(name, "drawable", getPackageName());
        return getResources().getDrawable(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addTopping(Topping topping) {
        for (PizzaPartImage pizzaPartImage : partImagesEnlarged) {
            pizza.getPizzaPart(pizzaPartImage.getPizzaPart()).addTopping(topping);
            pizzaPartImage.addTopping(topping, false);
        }
    }

    private void removeTopping(Topping topping) {
        for (PizzaPartImage pizzaPartImage : partImagesEnlarged) {
            pizza.getPizzaPart(pizzaPartImage.getPizzaPart()).removeTopping(topping);
            pizzaPartImage.removeTopping(topping);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void pizzaPartClicked(View view) {
        int clickId = view.getId();
        int pizzaPart = getPartFromId(clickId);
        partClickedAction(view, pizzaPart);
    }

    private int getPartFromId(int clickId) {
        for (PizzaPartImage pizzaPartImage : partImages) {
            if (pizzaPartImage.getId() == clickId) {
                return pizzaPartImage.getPizzaPart();
            }
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void partClickedAction(View view, int pizzaPartClicked) {
        PizzaPartImage partClicked = partImages.get(pizzaPartClicked);
        if (partImagesEnlarged.contains(partClicked)) {
            partClicked.shrinkSlice();
            partImagesEnlarged.remove(partClicked);
            if (partImagesEnlarged.size() == 0) {
                backToMain(view);
            }
        } else {
            partImagesEnlarged.add(partClicked);
            partClicked.enlargeSlice();
        }
        editToppingChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void editToppingChart() {
        for (Topping topping : toppingsList) {
            LinearLayout toppingBox = findViewById(toppingsList.indexOf(topping));
            for (PizzaPartImage pizzaPartImage : partImagesEnlarged) {
                if (!pizzaPartImage.hasTopping(topping)) {
                    removeToppingOnPizzaIndicatorFromToppingBox(toppingBox);
                    break;
                } else {
                    addToppingOnPizzaIndicatorToToppingBox(toppingBox);
                }
            }
        }
    }

    public void backToMain(View view) {
        Log.d(LOG_TAG, "backToMain");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pizza", pizza);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pizza", pizza);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
package com.pizzapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.IO;
import com.pizzapp.utilities.UI.Grid;
import com.pizzapp.utilities.UI.PizzaPartImage;
import com.pizzapp.utilities.UI.ToppingBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ToppingsPopUp extends AppCompatActivity implements Serializable {

    private static final String LOG_TAG = ToppingsPopUp.class.getSimpleName();

    private int currentSliceIdOutOfFour;
    private Pizza pizza;
    private List<PizzaPartImage> partImages = new ArrayList<>();
    private List<PizzaPartImage> partImagesEnlarged = new ArrayList<>();
    private List<Topping> toppingsList = new ArrayList<>();
    private Grid toppingGrid;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_toppings);
        initializePizzaPartImageList(findViewById(android.R.id.content).getRootView());
        toppingsList = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database)).getToppings();
        toppingGrid = new Grid(R.id.grid_layout, toppingsList, findViewById(android.R.id.content).getRootView());
        addGridOnClickListener();
        extractExtras();
    }

    private void initializePizzaPartImageList(View view) {
        partImages.add(new PizzaPartImage(R.id.topRightFrame, R.id.pizzaPLusTopRight, 0, R.id.topRight, "topRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomRightFrame, R.id.pizzaPLusBottomRight, 1, R.id.bottomRight, "bottomRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomLeftFrame, R.id.pizzaPLusBottomleft, 2, R.id.bottomLeft, "bottomLeft", view));
        partImages.add(new PizzaPartImage(R.id.topLeftFrame, R.id.pizzaPLusTopleft, 3, R.id.topLeft, "topLeft", view));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void extractExtras() {
        Bundle extras = getIntent().getExtras();
        currentSliceIdOutOfFour = extras.getInt("callingId");
        pizza = (Pizza) extras.getSerializable("pizza");
        createInitialPizza();
        partImagesEnlarged.add(partImages.get(currentSliceIdOutOfFour));
        partImages.get(currentSliceIdOutOfFour).enlargeSlice();
        toppingGrid.editToppingGrid(partImagesEnlarged);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createInitialPizza() {
        for (int i = 0; i < pizza.getNumberOfParts(); i++) {
            for (Topping topping : pizza.getParts().get(i).getToppings()) {
                partImages.get(i).addTopping(topping, true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addGridOnClickListener() {
        for (final ToppingBox toppingBox : toppingGrid.getToppingBoxList()) {
            LinearLayout box = toppingBox.getToppingBox();
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrRemoveTopping(toppingBox, toppingBox.getTopping());
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addOrRemoveTopping(ToppingBox toppingBox, Topping topping) {
        LinearLayout box = toppingBox.getToppingBox();
        if (box.getChildAt(0).getVisibility() == View.INVISIBLE) {
            removeTopping(topping);
            toppingBox.removeToppingOnPizzaIndicatorFromToppingBox();
        } else {
            for (PizzaPartImage pizzaPartImage : partImagesEnlarged) {
                if (!pizzaPartImage.hasTopping(topping)) {
                    addTopping(topping);
                    toppingBox.addToppingOnPizzaIndicatorToToppingBox();
                }
            }
        }
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
        toppingGrid.editToppingGrid(partImagesEnlarged);
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
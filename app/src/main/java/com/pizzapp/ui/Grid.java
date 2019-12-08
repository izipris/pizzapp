package com.pizzapp.ui;

import android.os.Build;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.pizzapp.model.pizza.Topping;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Grid {

    private static final int NUM_OF_ROWS = 4;
    int id;
    List<Topping> toppingList;
    List<ToppingBox> toppingBoxList = new ArrayList<>();
    View view;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Grid(int id, List<Topping> toppingList, View view) {
        this.id = id;
        this.toppingList = toppingList;
        this.view = view;
        createGrid();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createGrid() {
        GridLayout layout = view.findViewById(id);
        layout.setUseDefaultMargins(true);
        int numberOfRows = min(NUM_OF_ROWS, toppingList.size());
        layout.setRowCount(numberOfRows);
        int numberOfColumns = calculateNumberOfColumns();
        layout.setColumnCount(numberOfColumns);
        for (int i = 0; i < numberOfRows; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1, 0);
            for (int j = 0; j < numberOfColumns; j++) {
                if (j * NUM_OF_ROWS + i == toppingList.size()) {
                    break;
                }
                GridLayout.Spec colSpec = GridLayout.spec(j, 1, 1);
                final Topping topping = toppingList.get(NUM_OF_ROWS * j + i);
                ToppingBox toppingBox = new ToppingBox(topping, view, i, j, NUM_OF_ROWS);
                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
                myGLP.rowSpec = rowSpec;
                myGLP.columnSpec = colSpec;
                LinearLayout box = toppingBox.toppingBox;
                layout.addView(box, myGLP);
                toppingBoxList.add(toppingBox);
            }
        }
    }

    private int calculateNumberOfColumns() {
        int numberOfRows = toppingList.size() / NUM_OF_ROWS;
        if (toppingList.size() % NUM_OF_ROWS != 0) {
            numberOfRows++;
        }
        return numberOfRows;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void editToppingGrid(List<PizzaPartImage> partsEnlarged) {
        for (ToppingBox toppingBox : toppingBoxList) {
            for (PizzaPartImage pizzaPartImage : partsEnlarged) {
                if (!pizzaPartImage.hasTopping(toppingBox.getTopping())) {
                    toppingBox.removeToppingOnPizzaIndicatorFromToppingBox();
                    break;
                } else {
                    toppingBox.addToppingOnPizzaIndicatorToToppingBox();
                }
            }
        }
    }

    public List<ToppingBox> getToppingBoxList() {
        return toppingBoxList;
    }


}

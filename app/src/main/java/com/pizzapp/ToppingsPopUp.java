package com.pizzapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.DoesNotExist;
import com.pizzapp.utilities.IO;
import com.pizzapp.utilities.StaticFunctions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.min;

public class ToppingsPopUp extends AppCompatActivity implements Serializable {

    private static final String LOG_TAG = ToppingsPopUp.class.getSimpleName();

    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int ANGLE_TO_ROTATE = 90;
    private static final int PIZZA_PASSED = 3;
    private static final int ENLARGED_WIDTH = 165;
    private static final int ENLARGED_HEIGHT = 165;
    private static final int TOPPING_ENLARGED_WIDTH = 147;
    private static final int TOPPING_ENLARGED_HEIGHT = 147;
    private static final int ORIGINAL_WIDTH = 145;
    private static final int ORIGINAL_HEIGHT = 145;
    private static final int TOPPING_ORIGINAL_HEIGHT = 127;
    private static final int TOPPING_ORIGINAL_WIDTH = 127;

    private static final int MIN_ROWS_IN_CHART = 3;

    private int currentSliceId = -1;
    private int currentSliceIdOutOfFour;
    private Pizza pizza;
    private List<Topping> toppingsList = new ArrayList<>();
    private boolean initiationOfActivity = true;

    private Map<Integer, String> idToStringMap = new HashMap<>();
    private Map<Integer, Integer> pizzaImageToPartMap = new HashMap<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_toppings);
        setToolbar();
        initiateidToStringMap();
        initializepizzaImageToPartMap();
        extractExtras();
        toppingsList = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database)).getToppings();
        createToppingChart();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.topping_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void extractExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentSliceIdOutOfFour = extras.getInt("callingId");
            assignCurrentSliceId();

            if (extras.getInt("numberOfExtras") == PIZZA_PASSED) {
                pizza = (Pizza) extras.getSerializable("pizza");
                createInitialPizza(currentSliceId);
            } else {
                createDefaultPizza();
            }
            initiationOfActivity = false;
            enlargeSlice();
        }
    }

    private void createInitialPizza(int partId) {
        currentSliceIdOutOfFour = 0;
        assignCurrentSliceId();
        for (PizzaPart pizzaPart : pizza.getParts()) {
            for (Topping topping : pizzaPart.getToppings()) {
                addTopping(topping);
            }
            currentSliceIdOutOfFour++;
            assignCurrentSliceId();
        }
        currentSliceId = partId;
        assignCurrentSliceOutOfFour();
    }

    private void createDefaultPizza() {
        pizza = new Pizza(4, new Size(), new Crust());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createToppingChart() {
        GridLayout layout = findViewById(R.id.grid_layout);
        int numberOfRows = min(MIN_ROWS_IN_CHART, toppingsList.size());
        layout.setRowCount(numberOfRows);
        int numberOfColumns = calculateNumberOfColumns();
        layout.setColumnCount(numberOfColumns);
        for (int i = 0; i < numberOfRows; i++) {
            GridLayout.Spec rowSpec = GridLayout.spec(i, 1, 1);
            for (int j = 0; j < numberOfColumns; j++) {
                if (j * MIN_ROWS_IN_CHART + i == toppingsList.size()) {
                    break;
                }
                GridLayout.Spec colSpec = GridLayout.spec(j, 1, 1);
                CheckBox checkBox = createCheckbox(i, j);
                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
                myGLP.rowSpec = rowSpec;
                myGLP.columnSpec = colSpec;
                layout.addView(checkBox, myGLP);
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

    private CheckBox createCheckbox(int row, int col) {
        final CheckBox checkBox = new CheckBox(this);
        final Topping topping = toppingsList.get(MIN_ROWS_IN_CHART * col + row);
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        checkBox.setId(MIN_ROWS_IN_CHART * col + row);
        checkBox.setText(topping.getName());
        if (pizza.getPizzaPart(currentSliceIdOutOfFour).hasCertainTopping(topping.getName())) {
            checkBox.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    addTopping(topping);
                } else {
                    removeToppingFromPizza(topping);
                }
            }
        });
        return checkBox;
    }

    private void initiateidToStringMap() {
        addEntryToIdToStringMap(R.id.topRight, "topRight");
        addEntryToIdToStringMap(R.id.bottomRight, "bottomRight");
        addEntryToIdToStringMap(R.id.bottomLeft, "bottomLeft");
        addEntryToIdToStringMap(R.id.topLeft, "topLeft");
    }

    private void addEntryToIdToStringMap(int value, String key) {
        idToStringMap.put(value, key);
    }

    private void RemoveEntryFromIdToStringMap(String value) {
        idToStringMap.remove(getKeyFromValue(value));
    }

    private void initializepizzaImageToPartMap() {
        pizzaImageToPartMap.put(R.id.topRight, 0);
        pizzaImageToPartMap.put(R.id.bottomRight, 1);
        pizzaImageToPartMap.put(R.id.bottomLeft, 2);
        pizzaImageToPartMap.put(R.id.topLeft, 3);
    }

    private void shrinkSlice() {
        for (Topping topping : pizza.getPizzaPart(currentSliceIdOutOfFour).getToppings()) {
            shrinkImage(getToppingId(topping), true);
        }
        shrinkImage(currentSliceId, false);
    }

    private void shrinkImage(int imageId, boolean isTopping) {
        ImageView image = findViewById(imageId);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        int width, height;
        if (isTopping){
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

    private void enlargeSlice() {
        for (Topping topping : pizza.getPizzaPart(currentSliceIdOutOfFour).getToppings()) {
            enlargeImage(getToppingId(topping), true);
        }
        enlargeImage(currentSliceId, false);
    }

    private void enlargeImage(Integer sliceId, boolean isTopping) {
        ImageView image = findViewById(sliceId);
        ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
        int height;
        int width;
        if (isTopping){
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

    private void addTopping(Topping topping) {
        int toppingId = createToppingId(topping);
        while (idToStringMap.containsKey(toppingId)) {
            createToppingId(topping);
        }
        addEntryToIdToStringMap(toppingId, getCurrentSliceString() + topping.getName());
        addToppingToPizza(topping, toppingId);
    }

    private int createToppingId(Topping topping) {
        Random random = new Random();
        return random.nextInt() * (toppingsList.indexOf(topping) + random.nextInt());
    }

    private void addToppingToPizza(Topping topping, int toppingId) {
        try {
            FrameLayout frameLayout = getAppropriateFrameId();
            ImageView newTopping = new ImageView(this);
            newTopping.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
            newTopping.setRotation(getCurrentRotation());
            newTopping.setId(toppingId);
            newTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newId = getSliceFromTopping(v.getId());
                    if (newId != currentSliceId) {
                        shrinkSlice();
                    } else {
                        backToMain(v);
                    }
                    currentSliceId = newId;
                    assignCurrentSliceOutOfFour();
                    enlargeSlice();
                    editToppingChart();
                }
            });
            FrameLayout.LayoutParams layoutParams = new
                    FrameLayout.LayoutParams(StaticFunctions.convertDpToPx(154), StaticFunctions.convertDpToPx(154));
            setGravity(layoutParams);
            if (initiationOfActivity) {
                layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_HEIGHT);
                layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ORIGINAL_WIDTH);
            } else {
                layoutParams.height = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_HEIGHT);
                layoutParams.width = StaticFunctions.convertDpToPx(TOPPING_ENLARGED_WIDTH);
                pizza.getPizzaPart(currentSliceIdOutOfFour).addTopping(topping);
            }
            newTopping.setLayoutParams(layoutParams);
            frameLayout.addView(newTopping);
        } catch (DoesNotExist doesNotExist) {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        Toast toast = Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private int getSliceFromTopping(int toppingId) {
        if (StaticFunctions.isSubstring("topRight", idToStringMap.get(toppingId))) {
            return R.id.topRight;
        }
        if (StaticFunctions.isSubstring("bottomRight", idToStringMap.get(toppingId))) {
            return R.id.bottomRight;
        }
        if (StaticFunctions.isSubstring("bottomLeft", idToStringMap.get(toppingId))) {
            return R.id.bottomLeft;
        }
        if (StaticFunctions.isSubstring("topLeft", idToStringMap.get(toppingId))) {
            return R.id.topLeft;
        }
        return -1;
    }

    private void setGravity(FrameLayout.LayoutParams layoutParams) {
        switch (currentSliceIdOutOfFour) {
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

    private Drawable convertStringToDrawable(String name) {
        int id = getResources().getIdentifier(name, "drawable", getPackageName());
        return getResources().getDrawable(id);
    }

    private void removeToppingFromPizza(Topping topping) {
        pizza.getPizzaPart(currentSliceIdOutOfFour).removeTopping(topping);
        ImageView toppingToRemove = findViewById(getToppingId(topping));
        toppingToRemove.setVisibility(View.GONE);
        RemoveEntryFromIdToStringMap(getCurrentSliceString() + topping.getName());
    }

    public void onClick(View view) {
        int newId = view.getId();
        if (newId != currentSliceId && currentSliceId != -1){
            shrinkSlice();
        } else {
            backToMain(view);
        }
        currentSliceId = newId;
        assignCurrentSliceOutOfFour();
        enlargeSlice();
        editToppingChart();
    }

    private void editToppingChart() {
        for (Topping topping : toppingsList) {
            CheckBox checkBox = findViewById(toppingsList.indexOf(topping));
            if (pizza.getPizzaPart(currentSliceIdOutOfFour).hasCertainTopping(topping.getName())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }
    }

    private int getToppingId(Topping topping) {
        for (Map.Entry<Integer, String> entry : idToStringMap.entrySet()) {
            if ((getCurrentSliceString() + topping.getName()).equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private Integer getKeyFromValue(String value) {
        for (Map.Entry<Integer, String> entry : idToStringMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    private void assignCurrentSliceOutOfFour() {
        currentSliceIdOutOfFour = pizzaImageToPartMap.get(currentSliceId);
    }

    private void assignCurrentSliceId() {
        for (Map.Entry<Integer, Integer> entry : pizzaImageToPartMap.entrySet()) {
            if (entry.getValue() == currentSliceIdOutOfFour) {
                currentSliceId = entry.getKey();
            }
        }
    }

    private String getCurrentSliceString() {
        switch (currentSliceIdOutOfFour) {
            case (TOP_RIGHT_SLICE):
                return "topRight";
            case (BOTTOM_RIGHT_SLICE):
                return "bottomRight";
            case (BOTTOM_LEFT_SLICE):
                return "bottomLeft";
            case (TOP_LEFT_SLICE):
                return "topLeft";
        }
        return "";
    }

    private int getCurrentRotation() {
        return ANGLE_TO_ROTATE * currentSliceIdOutOfFour;
    }

    private FrameLayout getAppropriateFrameId() throws DoesNotExist {
        switch (currentSliceIdOutOfFour) {
            case (TOP_RIGHT_SLICE):
                return findViewById(R.id.topRightFrame);
            case (BOTTOM_RIGHT_SLICE):
                return findViewById(R.id.bottomRightFrame);
            case (BOTTOM_LEFT_SLICE):
                return findViewById(R.id.bottomLeftFrame);
            case (TOP_LEFT_SLICE):
                return findViewById(R.id.topLeftFrame);
        }
        throw new DoesNotExist();
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

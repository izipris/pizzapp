package com.pizzapp;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
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
    private final int ENLARGED_WIDTH = 143;
    private final int ENLARGED_HEIGHT = 143;
    private final int TOPPING_ENLARGED_WIDTH = 123;
    private final int TOPPING_ENLARGED_HEIGHT = 123;
    private final double ORIGINAL_WIDTH = 127.5;
    private final double ORIGINAL_HEIGHT = 127.5;
    private final double TOPPING_ORIGINAL_HEIGHT = 107.5;
    private final double TOPPING_ORIGINAL_WIDTH = 107.5;
    private final String TOPPING_PICKED_INDICATOR = "@drawable/clicked_button_1";

    private static final int MIN_ROWS_IN_CHART = 4;

    private int currentSliceId = -1;
    private int currentSliceIdOutOfFour;
    private Pizza pizza;
    private List<Topping> toppingsList = new ArrayList<>();
    private boolean initiationOfActivity = true;
    private List<Integer> pizzas_enlarged = new ArrayList<>();

    private Map<Integer, String> idToStringMap = new HashMap<>();
    private Map<Integer, Integer> pizzaImageToPartMap = new HashMap<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_toppings);
        setToolbar();
        initiateIdToStringMap();
        initializePizzaImageToPartMap();
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
            pizzas_enlarged.add(currentSliceIdOutOfFour);
            assignCurrentSliceId();
            pizza = (Pizza) extras.getSerializable("pizza");
            createInitialPizza(currentSliceId);
            initiationOfActivity = false;
            enlargeSlice(currentSliceIdOutOfFour);
        }
    }

    private void createInitialPizza(int partId) {
        currentSliceIdOutOfFour = 0;
        assignCurrentSliceId();
        for (int i = 0; i < pizza.getNumberOfParts(); i++) {
            for (Topping topping : pizza.getParts().get(i).getToppings()) {
                addTopping(topping, i);
            }
            currentSliceIdOutOfFour++;
            assignCurrentSliceId();
        }
        currentSliceId = partId;
        assignCurrentSliceOutOfFour();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createToppingChart() {
        GridLayout layout = findViewById(R.id.grid_layout);
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
//        GridLayout.LayoutParams textParams = LinearLayout.getnew GridLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,
//                GridLayout.LayoutParams.WRAP_CONTENT );
//        textParams.gravity = Gravity.CENTER;
//        iconText.setLayoutParams(textParams);
//        toppingBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
        toppingBox.setPadding(StaticFunctions.convertDpToPx(20),StaticFunctions.convertDpToPx(10),
                StaticFunctions.convertDpToPx(20),StaticFunctions.convertDpToPx(10));
        toppingBox.setId(MIN_ROWS_IN_CHART * col + row);
        toppingBox.setOrientation(LinearLayout.HORIZONTAL);

        addToppingToBox(toppingBox, topping);

        if (pizza.getPizzaPart(currentSliceIdOutOfFour).hasCertainTopping(topping.getName())) {
            addIndicator(toppingBox);
        }

        toppingBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 04 דצמבר 2019 add the listener if has toppings already
                if (toppingBox.getBackground() == convertStringToDrawable(TOPPING_PICKED_INDICATOR)) {
                    removeTopping(topping);
                    removeIndicator(toppingBox);
                } else {
                    for (Integer pizzaPart : pizzas_enlarged) {
                        if (pizza.getPizzaPart(pizzaPart).hasCertainTopping(topping.getName())) {
                            continue;
                        }
                        addTopping(topping, pizzaPart);
                        addIndicator(toppingBox);
                    }
                }
            }
        });
        return toppingBox;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addIndicator(LinearLayout toppingBox) {
        toppingBox.getChildAt(0).setVisibility(View.GONE);
        toppingBox.setBackground(convertStringToDrawable(TOPPING_PICKED_INDICATOR));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void removeIndicator(LinearLayout toppingBox) {
        toppingBox.getChildAt(0).setVisibility(View.VISIBLE);
        toppingBox.setBackground(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingToBox(final LinearLayout toppingBox, final Topping topping){
        ImageView iconTopping = new ImageView(this);
        iconTopping.setImageDrawable(convertStringToDrawable(topping.getIconSource()));
        iconTopping.setPadding(5, 0, StaticFunctions.convertDpToPx(10), 0);
        iconTopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer pizzaPart : pizzas_enlarged) {
                    if (pizza.getPizzaPart(pizzaPart).hasCertainTopping(topping.getName())) {
                        continue;
                    }
                    addTopping(topping, pizzaPart);
                    addIndicator(toppingBox);
                }
            }
        });
//        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT );
        TextView iconText = new TextView(this);
        String textToDisplay = topping.getName() + "......" + topping.getPrice()/4;
                iconText.setText(textToDisplay);
        iconText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Integer pizzaPart : pizzas_enlarged) {
                    if (pizza.getPizzaPart(pizzaPart).hasCertainTopping(topping.getName())) {
                        continue;
                    }
                    addTopping(topping, pizzaPart);
                    addIndicator(toppingBox);
                }
            }
        });
//        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT );
//        textParams.gravity = Gravity.CENTER;
//        iconText.setLayoutParams(textParams);
        toppingBox.addView(iconTopping);
        toppingBox.addView(iconText);
    }


    private void initiateIdToStringMap() {
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

    private void initializePizzaImageToPartMap() {
        pizzaImageToPartMap.put(R.id.topRight, 0);
        pizzaImageToPartMap.put(R.id.bottomRight, 1);
        pizzaImageToPartMap.put(R.id.bottomLeft, 2);
        pizzaImageToPartMap.put(R.id.topLeft, 3);
    }

    private void shrinkSlice(int pizzaPart) {
        for (Topping topping : pizza.getPizzaPart(pizzaPart).getToppings()) {
            shrinkImage(getToppingId(topping, pizzaPart), true);
        }
        shrinkImage(getIdFromPart(pizzaPart), false);
    }

    private void shrinkImage(int imageId, boolean isTopping) {
        ImageView image = findViewById(imageId);
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

    private void enlargeSlice(int pizzaPart) {
        for (Topping topping : pizza.getPizzaPart(pizzaPart).getToppings()) {
            enlargeImage(getToppingId(topping, pizzaPart), true);
        }
        enlargeImage(getIdFromPart(pizzaPart), false);
    }

    private void enlargeImage(Integer sliceId, boolean isTopping) {
        ImageView image = findViewById(sliceId);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addTopping(Topping topping, int pizzaPart) {
        int toppingId = createToppingId(topping);
        while (idToStringMap.containsKey(toppingId)) {
            createToppingId(topping);
        }
        addEntryToIdToStringMap(toppingId, getCurrentSliceString(pizzaPart) + topping.getName());
        addToppingToPizza(topping, toppingId, pizzaPart);
    }

    private int createToppingId(Topping topping) {
        Random random = new Random();
        return random.nextInt() * (toppingsList.indexOf(topping) + random.nextInt());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addToppingToPizza(Topping topping, int toppingId, int pizzaPart) {
        try {
            FrameLayout frameLayout = getAppropriateFrameId(pizzaPart);
            ImageView newTopping = new ImageView(this);
            newTopping.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
            newTopping.setRotation(getCurrentRotation(pizzaPart));
            newTopping.setId(toppingId);
            newTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int partClickedId = getSliceFromTopping(v.getId());
                    partClickedAction(v, partClickedId);
                }
            });
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
                pizza.getPizzaPart(pizzaPart).addTopping(topping);
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

    private Drawable convertStringToDrawable(String name) {
        int id = getResources().getIdentifier(name, "drawable", getPackageName());
        return getResources().getDrawable(id);
    }

    private void removeTopping(Topping topping) {
        for (Integer pizzaPart : pizzas_enlarged) {
            pizza.getPizzaPart(pizzaPart).removeTopping(topping);
            ImageView toppingToRemove = findViewById(getToppingId(topping, pizzaPart));
            toppingToRemove.setVisibility(View.GONE);
            RemoveEntryFromIdToStringMap(getCurrentSliceString(pizzaPart) + topping.getName());
        }
    }

    public void onClick(View view) {
        int clickId = view.getId();
        partClickedAction(view, clickId);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void partClickedAction(View view, int clickId) {
        if (pizzas_enlarged.contains(pizzaImageToPartMap.get(clickId))) {
            shrinkSlice(pizzaImageToPartMap.get(clickId));
            pizzas_enlarged.remove(pizzaImageToPartMap.get(clickId));
            if (pizzas_enlarged.size() == 0) {
                backToMain(view);
            }
        } else {
            pizzas_enlarged.add(pizzaImageToPartMap.get(clickId));
            currentSliceId = clickId;
            assignCurrentSliceOutOfFour();
            enlargeSlice(pizzaImageToPartMap.get(clickId));
        }
        editToppingChart();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void editToppingChart() {
        for (Topping topping : toppingsList) {
            LinearLayout toppingBox = findViewById(toppingsList.indexOf(topping));
            for (Integer pizzaPart : pizzas_enlarged) {
                if (!pizza.getPizzaPart(pizzaPart).hasCertainTopping(topping.getName())) {
                    removeIndicator(toppingBox);
                    break;
                } else {
                    addIndicator(toppingBox);
                }
            }
        }
    }

    private int getIdFromPart(int pizzaPart) {
        for (Integer id : pizzaImageToPartMap.keySet()) {
            if (pizzaImageToPartMap.get(id) == pizzaPart) {
                return id;
            }
        }
        return -1;
    }

    private int getToppingId(Topping topping, int pizzaPart) {
        for (Map.Entry<Integer, String> entry : idToStringMap.entrySet()) {
            if ((getCurrentSliceString(pizzaPart) + topping.getName()).equals(entry.getValue())) {
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

    private String getCurrentSliceString(int pizzaPart) {
        switch (pizzaPart) {
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

    private int getCurrentRotation(int pizzaPart) {
        return ANGLE_TO_ROTATE * pizzaPart;
    }

    private FrameLayout getAppropriateFrameId(int pizzaPart) throws DoesNotExist {
        switch (pizzaPart) {
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

//
//
//<LinearLayout
//            android:layout_width="wrap_content"
//                    android:layout_height="wrap_content"
//                    android:layout_row="0"
//                    android:layout_column="0"
//                    android:orientation="horizontal">
//
//<ImageView
//                android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        app:srcCompat="@drawable/olives_small_color"
//                        android:paddingRight="10dp"/>
//
//<TextView
//                android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        android:text="olives ....  2.00$"/>
//
//</LinearLayout>
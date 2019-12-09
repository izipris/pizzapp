package com.pizzapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.StaticFunctions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = OrderSummary.class.getSimpleName();
    private static final int ANGLE_TO_ROTATE = 90;
    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int SIZE_OF_TOPPING = 50;
    private Button mDelivery;
    private Button mPickup;
    private Order finalOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        initializeDataMembers();
        Intent intent = getIntent();
        ((TextView) findViewById(R.id.textViewGreeting)).setText(R.string.order_summary_title);
        finalOrder = (Order) intent.getSerializableExtra("order");
        if (finalOrder != null) {
            double testOrderTotalPrice = finalOrder.getTotalPrice();
            viewOrder();
            setPriceTextView(testOrderTotalPrice);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(this, intent);
        return true;
    }

    private void initializeDataMembers() {
        mPickup = findViewById(R.id.pickup_button);
        mDelivery = findViewById(R.id.delivery_button);
    }

    private void setPriceTextView(double testOrderTotalPrice) {
        TextView orderPrice = findViewById(R.id.order_total_price);
        orderPrice.setText(getString(R.string.price_showing_prefix)
                + testOrderTotalPrice + getString(R.string.price_showing_suffix));
    }

    private void viewOrder() {
        try {
            for (int i = 0; i < finalOrder.getNumberOfPizzas(); i++) {
                Pizza pizza = finalOrder.getPizza(i);
                LinearLayout pizzaText = findViewById(R.id.pizza_text);
                addDetailsToPizza(pizza, pizzaText);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDetailsToPizza(Pizza pizza, LinearLayout pizzaText) {
        addTextToPizza(pizza, pizzaText);
        addTopppingsToPizzaImage(pizza);
    }

    private void addTopppingsToPizzaImage(Pizza pizza) {
        FrameLayout topRightFrame = findViewById(R.id.top_right_part);
        PizzaPart topRightPizzaPart = pizza.getPizzaPart(0);
        addToppingsImageOnPart(topRightFrame, topRightPizzaPart);

        FrameLayout bottomRightFrame = findViewById(R.id.bottom_right_part);
        PizzaPart bottomRightPizzaPart = pizza.getPizzaPart(1);
        addToppingsImageOnPart(bottomRightFrame, bottomRightPizzaPart);

        FrameLayout bottomLeftFrame = findViewById(R.id.bottom_left_part);
        PizzaPart bottomLeftPizzaPart = pizza.getPizzaPart(2);
        addToppingsImageOnPart(bottomLeftFrame, bottomLeftPizzaPart);

        FrameLayout topLeftFrame = findViewById(R.id.top_left_part);
        PizzaPart topLeftPizzaPart = pizza.getPizzaPart(3);
        addToppingsImageOnPart(topLeftFrame, topLeftPizzaPart);
    }

    private void addToppingsImageOnPart(FrameLayout frame, PizzaPart part) {
        int partId = part.getId();

        for (Topping topping : part.getToppings()) {
            ImageView toppingImage = new ImageView(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    StaticFunctions.convertDpToPx(SIZE_OF_TOPPING)
                    , StaticFunctions.convertDpToPx(SIZE_OF_TOPPING));
            toppingImage.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
            toppingImage.setRotation(getCurrentRotation(partId));
            toppingImage.setScaleType(ImageView.ScaleType.FIT_START);
            setGravity(params, partId);
            toppingImage.setLayoutParams(params);
            toppingImage.setVisibility(View.VISIBLE);
            frame.addView(toppingImage);
            toppingImage.bringToFront();
        }
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

    private int getCurrentRotation(int pizzaPart) {
        return ANGLE_TO_ROTATE * pizzaPart;
    }

    private Drawable convertStringToDrawable(String name) {
        int id = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
        return this.getResources().getDrawable(id);
    }

    private void addTextToPizza(Pizza pizza, LinearLayout pizzaText) {
        setHeadline(pizza);  // static
        setToppings(pizza, pizzaText);  // dynamic
        setPizzaPrice(pizza, pizzaText);  // static
    }

    private void setToppings(Pizza pizza, LinearLayout pizzaText) {
        List<Topping> toppings = getAllToppings(pizza);  // including repetitions
        Set<String> alreadyInsertedToppings = new HashSet<>();
        if (toppings.size() == 0) {
            findViewById(R.id.textViewOrderSummaryToppingsHeader).setVisibility(View.INVISIBLE);
        }
        for (Topping topping : toppings) {
            String toppingName = topping.getName();
            if (!alreadyInsertedToppings.contains(toppingName)) {
                TextView toppingView = getToppingView(topping);
                pizzaText.addView(toppingView);
                alreadyInsertedToppings.add(toppingName);
            }
        }
    }

    private List<Topping> getAllToppings(Pizza pizza) {
        LinkedList<Topping> toppings = new LinkedList<>();
        for (PizzaPart part : pizza.getParts()) {
            if (part.isHasTopping())
                toppings.addAll(part.getToppings());
        }
        return toppings;
    }


    private void setPizzaPrice(Pizza pizza, LinearLayout pizzaText) {
        TextView priceView = getPizzaPriceView(pizza);
        pizzaText.addView(priceView);
    }


    private TextView getPizzaPriceView(Pizza pizza) {
        double price = pizza.getPrice();
        TextView priceView = getAnonymousTextView();
        priceView.setText(getString(R.string.price_showing_prefix) + price + getString(R.string.price_showing_suffix) + "   ");
        priceView.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            priceView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        priceView.setTextColor(getResources().getColor(R.color.cheese_maize));
        priceView.setTypeface(null, Typeface.BOLD);
        priceView.setVisibility(View.VISIBLE);
        return priceView;
    }


    private void setHeadline(Pizza pizza) {
        TextView size = findViewById(R.id.size_of_pizza);
        size.setText(pizza.getSize().getCaption());
        TextView crust = findViewById(R.id.crust_of_pizza);
        crust.setText(pizza.getCrust().getName());
    }


    public TextView getAnonymousTextView() {
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        return textView;
    }


    private TextView getToppingView(Topping topping) {
        TextView toppingsView = getAnonymousTextView();
        toppingsView.setTextSize(8);
        toppingsView.setText(getString(R.string.orderSummaryToppingPrefix) + topping.getName() + "..." + topping.getPrice() + getString(R.string.currency_symbol));
        toppingsView.setVisibility(View.VISIBLE);
        return toppingsView;
    }


    public void launchDeliveryActivity(View view) {
        Log.d(LOG_TAG, "Delivery button clicked!");
        mDelivery.setTextColor(Color.RED);
        Intent intent = new Intent(this, Delivery.class);
        startActivity(intent);
    }


    public void launchPickupActivity(View view) {
        Log.d(LOG_TAG, "Pickup button clicked!");
        mPickup.setTextColor(Color.RED);
        Intent intent = new Intent(this, Pickup.class);
        startActivity(intent);
    }

    public void backToMain(View view) {
        Log.d(LOG_TAG, "backToMain");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pizza", finalOrder.getLastPizza());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        mDelivery.setTextColor(Color.BLACK);
        mPickup.setTextColor(Color.BLACK);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }


}

package com.pizzapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;

import java.util.LinkedList;
import java.util.List;


public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = OrderSummary.class.getSimpleName();
    private LinearLayout mPizzaLayout;
    private Button mDelivery;
    private Button mPickup;
    private Order finalOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        initializeDataMembers();
        Intent intent = getIntent();

        finalOrder = (Order) intent.getSerializableExtra("order");

        if (finalOrder != null) {
            double testOrderTotalPrice = finalOrder.getTotalPrice();
            viewOrder(finalOrder);
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
        mPizzaLayout = findViewById(R.id.pizza_text_layout);
        mPickup = findViewById(R.id.pickup_button);
        mDelivery = findViewById(R.id.delivery_button);
    }

    private void setPriceTextView(double testOrderTotalPrice) {
        TextView orderPrice = findViewById(R.id.order_total_price);
        orderPrice.setText("Order Total: " + testOrderTotalPrice + "$");
    }

    private void viewOrder(Order testOrder) {
        try {
            for (int i = 0; i < testOrder.getNumberOfPizzas(); i++) {
                List<TextView> pizzaViews = getPizzaViews(testOrder.getPizza(i));
                for (TextView view : pizzaViews)
                    mPizzaLayout.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextView getSizeView(Pizza pizza) {
        TextView pizzaSizeView = getAnonymousTextView();
        pizzaSizeView.setText(pizza.getSize().getCaption() + "Pizza | "
                + pizza.getCrust().getName() + " Crust");
        pizzaSizeView.setTextSize(17);
        pizzaSizeView.setPadding(5, 5, 5, 5);
        pizzaSizeView.setVisibility(View.VISIBLE);
        return pizzaSizeView;
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

    private TextView getPartLineView(int part_i, int numOfParts) {
        TextView partLineView = getAnonymousTextView();
        partLineView.setTextSize(13);
        partLineView.setText("Part " + (part_i + 1) + "/" + numOfParts + ":");
        partLineView.setVisibility(View.VISIBLE);
        return partLineView;
    }

    private TextView getToppingView(Topping topping) {
        TextView toppingsView = getAnonymousTextView();
        toppingsView.setTextSize(8);
        toppingsView.setText(topping.getName() + "..." + topping.getPrice());
        toppingsView.setVisibility(View.VISIBLE);
        return toppingsView;
    }

    private List<TextView> getPartToppings(PizzaPart currPart) {
        List<TextView> toppings = new LinkedList<>();
        for (Topping topping : currPart.getToppings()) {
            toppings.add(getToppingView(topping));
        }
        return toppings;
    }

    private List<TextView> getPartAndToppingsViews(int i, Pizza pizza) {
        List<TextView> partAndTopping = new LinkedList<>();
        int numOfParts = pizza.getNumberOfParts();
        PizzaPart currPart = pizza.getPizzaPart(i);

        TextView pizzaPartLineView = getPartLineView(i, numOfParts);
        List<TextView> toppingsViews = getPartToppings(currPart);

        partAndTopping.add(pizzaPartLineView);
        partAndTopping.addAll(toppingsViews);
        return partAndTopping;
    }

    private List<TextView> getAllPartsAndToppings(Pizza pizza, int numOfParts) {
        List<TextView> partsAndToppings = new LinkedList<>();
        for (int i = 0; i < numOfParts; i++) {
            List<TextView> partAndTopping = getPartAndToppingsViews(i, pizza);
            partsAndToppings.addAll(partAndTopping);
        }
        return partsAndToppings;
    }


    private List<TextView> getPizzaViews(Pizza pizza) {
        int numOfParts = pizza.getNumberOfParts();
        List<TextView> views = new LinkedList<>();
        views.add(getSizeView(pizza));
        views.addAll(getAllPartsAndToppings(pizza, numOfParts));
        views.add(getPizzaPriceView(pizza, numOfParts));
        return views;
    }


    private TextView getPizzaPriceView(Pizza pizza, int numOfParts) {
        double price = pizza.getPrice();
        TextView priceView = getAnonymousTextView();
        priceView.setText("Total:........." + price + "$");
        priceView.setTextSize(15);
        priceView.setTypeface(null, Typeface.BOLD);
        priceView.setVisibility(View.VISIBLE);
        return priceView;
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

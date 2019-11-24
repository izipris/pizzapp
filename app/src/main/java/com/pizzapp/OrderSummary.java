package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;


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
import androidx.appcompat.widget.Toolbar;

import com.pizzapp.model.Database;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;

import com.pizzapp.utilities.IO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private LinearLayout mPizzaLayout;
    private Button mDelivery;
    private Button mPickup;
    private Toolbar toolbar;
    private Order finalOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        setToolbar();

        initializeDataMembers();
        Intent intent = getIntent();

        Order finalOrder = (Order) intent.getSerializableExtra("order");

        if (finalOrder != null) {
            double testOrderTotalPrice = finalOrder.getTotalPrice();
            viewOrder(finalOrder);
            setPriceTextView(testOrderTotalPrice);
        }
    }

    public void setToolbar(){
        toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void initializeDataMembers(){
        mPizzaLayout = findViewById(R.id.pizza_text_layout);
        mPickup = findViewById(R.id.pickup_button);
        mDelivery = findViewById(R.id.delivery_button);
    }

    private void setPriceTextView(double testOrderTotalPrice){
        TextView orderPrice = findViewById(R.id.order_total_price);
        orderPrice.setText("Order Total: " + String.valueOf(testOrderTotalPrice) + "$");
    }

    private void viewOrder(Order testOrder) {
        try{
            for (int i=0; i < testOrder.getNumberOfPizzas(); i++){
                List<TextView> pizzaViews = getPizzaViews(testOrder.getPizza(i));
                for (TextView view: pizzaViews)
                    mPizzaLayout.addView(view);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private TextView getSizeView(Pizza pizza){
        TextView pizzaSizeView = getAnonymousTextView();
        pizzaSizeView.setText(pizza.getSize().getName() + " "
                            + pizza.getCrust().getName() + " Pizza"); //don't care about translations
        pizzaSizeView.setTextSize(20);
        pizzaSizeView.setPadding(5,5,5,5);
        pizzaSizeView.setVisibility(View.VISIBLE);
        return pizzaSizeView;
    }

    public LinearLayout getPizzaLayout(){
        LinearLayout genLayout = new LinearLayout(this);
        genLayout.setOrientation(LinearLayout.VERTICAL);
        genLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        genLayout.setPadding(20, 20, 20, 20);
        return  genLayout;
    }

    public TextView getAnonymousTextView(){
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,0,0,0);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private TextView getPartLineView(int part_i, int numOfParts){
        TextView partLineView = getAnonymousTextView();
        partLineView.setTextSize(13);
        partLineView.setText("Part " + (part_i + 1) + "/" + numOfParts + ":");
        partLineView.setVisibility(View.VISIBLE);
        return partLineView;
    }

    private TextView getToppingView(Topping topping){
        TextView toppingsView = getAnonymousTextView();
        toppingsView.setTextSize(8);
        toppingsView.setText(topping.getName() + "..." +
                String.valueOf(topping.getPrice()));
        toppingsView.setVisibility(View.VISIBLE);
        return toppingsView;
    }

    private List<TextView> getPartToppings(PizzaPart currPart) {
        List<TextView> toppings = new LinkedList<>();
        for (Topping topping: currPart.getToppings()){
            toppings.add(getToppingView(topping));
        }
        return  toppings;
    }

    private List<TextView> getPartAndToppingsViews(int i, Pizza pizza){
//        LinearLayout partLayout = getPizzaLayout();
        List<TextView>  partAndTopping = new LinkedList<>();
        int numOfParts = pizza.getNumberOfParts();
        PizzaPart currPart = pizza.getPizzaPart(i);

        TextView pizzaPartLineView = getPartLineView(i, numOfParts);
        List<TextView> toppingsViews = getPartToppings(currPart);

        partAndTopping.add(pizzaPartLineView);
        partAndTopping.addAll(toppingsViews);
        return partAndTopping;
    }

    private List<TextView> getAllPartsAndToppings(Pizza pizza, int numOfParts) {
        List<TextView>  partsAndToppings = new LinkedList<>();
        for (int i = 0; i < numOfParts; i++){
            List<TextView> partAndTopping = getPartAndToppingsViews(i, pizza);
            partsAndToppings.addAll(partAndTopping);
        }
        // essentially will return a list of view of part,topping,part,topping
        return partsAndToppings;
    }



    private List<TextView> getPizzaViews(Pizza pizza) {
        int numOfParts = pizza.getNumberOfParts();
        List<TextView>  views = new LinkedList<>();
        views.add(getSizeView(pizza));
        views.addAll(getAllPartsAndToppings(pizza, numOfParts));
        views.add(getPizzaPriceView(pizza, numOfParts));
        return views;
    }

    private double getPizzaPrice(Pizza pizza, int numOfParts){
        double priceSum = pizza.getSize().getPrice();
        for (int i = 0; i < numOfParts; i++){
            List<Topping> toppings = pizza.getPizzaPart(i).getToppings();
            for (Topping topping: toppings)
                priceSum = priceSum + topping.getPrice();
        }
        return priceSum;
    }

    private TextView getPizzaPriceView(Pizza pizza, int numOfParts) {
        double price = getPizzaPrice(pizza, numOfParts);
        TextView priceView = getAnonymousTextView();
        priceView.setText("Total:........." + String.valueOf(price) + "$");
        priceView.setTextSize(15);
        priceView.setTypeface(null, Typeface.BOLD);
        priceView.setVisibility(View.VISIBLE);
        return priceView;
    }

    private Pizza getTestPizza(int i_size, int i_crust, int i_toppins, int num_parts){
        Database db = getDatabase();
        Pizza testPizza = new Pizza(num_parts, db.getSizes().get(i_size),db.getCrusts().get(i_crust));
        for (int i = 0; i < num_parts; i++){
            PizzaPart testPizzaPart = testPizza.getPizzaPart(i);
            if (!testPizzaPart.isHasTopping()){
                testPizzaPart.addTopping(db.getToppings().get(i_toppins));
            }
        }
        return testPizza;
    }

    private Database getDatabase() {
        return IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
    }

    private Order getTestOrder() {
        try{

            Pizza testPizza1 = getTestPizza(0,0,0,1);
            Pizza testPizza2 = getTestPizza(1,1,1,2);
            Pizza testPizza3 = getTestPizza(2,2,2,4);
            Pizza testPizza4 = getTestPizza(2,2,2,6);
            Order testOrder = new Order(1234);
            testOrder.addPizza(testPizza1);
            testOrder.addPizza(testPizza2);
            testOrder.addPizza(testPizza3);
            testOrder.addPizza(testPizza4);
            return testOrder;
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }


    public void launchDeliveryActivity(View view){
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

    @Override
    public void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        mDelivery.setTextColor(Color.BLACK);
        mPickup.setTextColor(Color.BLACK);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    public String byteBufferToString(byte[] buffer){
        try{
            String order_as_json = new String(buffer, "UTF-8");
            return order_as_json;
        } catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public String loadJSONToString() {
        try {
            InputStream is = getAssets().open("order.json");
            byte[] buffer = new byte[is.available()]; // todo: they say not to use but cannot find alternative to go with android 15 and up
            int bytes_read = is.read(buffer);
            if(bytes_read == 0 || buffer.length == 0){
                Log.d(LOG_TAG, "empty or zero bytes or");
                return null;  // todo
            }
            is.close();
            return byteBufferToString(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }


}

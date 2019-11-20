package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pizzapp.model.Database;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.utilities.IO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ScrollView mOrderDetailsScrollText;
    private LinearLayout mPizzaLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        mOrderDetailsScrollText = findViewById(R.id.order_details_scroll_text);
        mPizzaLayout = findViewById(R.id.pizza_text_layout);

        Order testOrder = getTestOrder();
        double testOrderTotalPrice = testOrder.getTotalPrice();
        viewOrder(testOrder);
        setPriceTextView(testOrderTotalPrice);
    }

    private void setPriceTextView(double testOrderTotalPrice){
        TextView orderPrice = findViewById(R.id.order_total_price);
        orderPrice.setText("Order Total:" + String.valueOf(testOrderTotalPrice) + "$");
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
        pizzaSizeView.setTextSize(25); // todo verify font family
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

    private TextView getPartLineView(){
        TextView partLineView = getAnonymousTextView();
        partLineView.setTextSize(18); // todo verify font family
        return partLineView;
    }

    private TextView getPartToppings(PizzaPart currPart) {
        TextView toppingsView = getAnonymousTextView();
        toppingsView.setTextSize(13); // todo verify font family
        toppingsView.setText(currPart.getTopping().getName() + "..." +
                String.valueOf(currPart.getTopping().getPrice()));
        return toppingsView;
    }

    private List<TextView> getPartLayout(int i, Pizza pizza){
//        LinearLayout partLayout = getPizzaLayout();
        List<TextView>  partAndTopping = new LinkedList<>();
        int numOfParts = pizza.getNumberOfParts();
        PizzaPart currPart = pizza.getPizzaPart(i);

        TextView pizzaPartLineView = getPartLineView();
        TextView toppingsView = getPartToppings(currPart);
        pizzaPartLineView.setText("Part " + i + "/" + numOfParts + ":");
        pizzaPartLineView.setVisibility(View.VISIBLE);
        toppingsView.setVisibility(View.VISIBLE);
        partAndTopping.add(pizzaPartLineView);
        partAndTopping.add(toppingsView);
        return partAndTopping;
    }

    private List<TextView> getAllPartsAndToppings(Pizza pizza, int numOfParts) {
        List<TextView>  partsAndToppings = new LinkedList<>();
        for (int i = 0; i < numOfParts; i++){
            List<TextView> partAndTopping = getPartLayout(i, pizza);
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
        for (int i = 0; i < numOfParts; i++)
            priceSum = priceSum + pizza.getPizzaPart(i).getTopping().getPrice();
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

    private Pizza getTestPizza(){
        Database db = getDatabase();
        Pizza testPizza = new Pizza(db.getSizes().get(0),db.getCrusts().get(0));
        PizzaPart testPizzaPart = testPizza.getPizzaPart(0);
        if (!testPizzaPart.isHasTopping()){
            testPizzaPart.setTopping(db.getToppings().get(0));
        }
        return testPizza;
    }

    private Database getDatabase() {
        return IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
    }

    private Order getTestOrder() {
        Pizza testPizza = getTestPizza();
        Order testOrder = new Order(1234);
        testOrder.addPizza(testPizza);
        return testOrder;
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

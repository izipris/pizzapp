package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pizzapp.model.Database;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.utilities.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.util.LinkedList;

public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        Order testOrder = getTestOrder();
        double testOrderTotalPrice = testOrder.getTotalPrice();
        TextView orderDetails = findViewById(R.id.order_details);

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

    private Order getTestOrder() {
        Pizza testPizza = getTestPizza();
        Order testOrder = new Order(1234);
        testOrder.addPizza(testPizza);
        return testOrder;
    }

    private Database getDatabase() {
        return IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database));
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

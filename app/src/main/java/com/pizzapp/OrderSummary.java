package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
//import android.widget.TextView;
//
//import org.json.JSONObject;
//


public class OrderSummary extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
//        ActivityOrderSummaryBinding binding = DataBindingUtil.setContentView(
//                this, R.layout.activity_order_summary);
//        binding.setOrder(order);// todo not sure what this does
//        Part part = new Part(4, "pineapple");
//        order = new Order(123);
//        TextView textView = findViewById(R.id.order_text);
//        textView.setText(order.toString());
    }


}

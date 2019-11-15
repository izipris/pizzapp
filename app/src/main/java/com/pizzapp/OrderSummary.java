package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.pizzapp.databinding.ActivityOrderSummaryBinding;

import org.json.JSONObject;

import static com.pizzapp.BR.order;

public class OrderSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        ActivityOrderSummaryBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_order_summary);
        Order order = new Order(123);  // todo not sure what this does
        binding.setOrder(order);

    }


}

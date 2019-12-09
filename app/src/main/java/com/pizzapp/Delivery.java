package com.pizzapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Delivery extends AppCompatActivity {

    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        TextView textView = findViewById(R.id.textViewGreeting);
        textView.setText(R.string.delivery);

        total = getIntent().getDoubleExtra("Total", 0);

    }

    public void toPayment(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("Total", total);
        startActivity(intent);
    }
}

package com.pizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Pickup extends AppCompatActivity {

    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        TextView textView = findViewById(R.id.textViewGreeting);
        textView.setText(R.string.delivery_header_text);

        total = getIntent().getDoubleExtra("Total", 0);
    }

    public void toPayment(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("Total", total);
        startActivity(intent);
    }
}

package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView textView = findViewById(R.id.textViewGreeting);
        textView.setText(R.string.payment_header_text);

        double total = getIntent().getDoubleExtra("Total",0);
        String totalText = getResources().getString(R.string.price_showing_prefix) + total + getResources().getString(R.string.price_showing_suffix);
        textView = findViewById(R.id.textViewTotal);
        textView.setText(totalText);
    }

    public void onClickPay(View view){
        final Dialog successfulPayment = new Dialog(this);
        successfulPayment.setContentView(R.layout.successful_payment);
        Button okButton = findViewById(R.id.doneButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successfulPayment.dismiss();
            }
        });
        successfulPayment.show();

    }
}

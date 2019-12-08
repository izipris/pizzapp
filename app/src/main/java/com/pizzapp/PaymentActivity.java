package com.pizzapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private Dialog successfulPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView textView = findViewById(R.id.textViewGreeting);
        textView.setText(R.string.payment_header_text);

        double total = getIntent().getDoubleExtra("Total", 0);
        String totalText = getResources().getString(R.string.price_showing_prefix) + total + getResources().getString(R.string.price_showing_suffix);
        textView = findViewById(R.id.textViewTotal);
        textView.setText(totalText);
    }

    public void onClickPay(View view){
        successfulPayment = new Dialog(this);
        successfulPayment.setContentView(R.layout.successful_payment);
        successfulPayment.show();
    }

    public void done(View view){
        successfulPayment.dismiss();
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }
}

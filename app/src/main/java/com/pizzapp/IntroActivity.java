package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
    private static final String LOG_TAG = IntroActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void launchMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("newOrder", true);
        startActivity(intent);
    }
}

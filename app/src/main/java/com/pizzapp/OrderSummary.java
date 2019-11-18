package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

//import android.widget.TextView;
//import org.json.JSONObject;


public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

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


    public String loadJSONFromAsset(Context context) {
        try {
            InputStream is = context.getAssets().open("order.json");
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

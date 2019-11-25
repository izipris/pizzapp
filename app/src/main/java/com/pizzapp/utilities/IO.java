package com.pizzapp.utilities;

import com.pizzapp.model.Database;
import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.model.pizza.Topping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

public class IO {
    public static String readInputStreamToString(InputStream is) throws IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        return writer.toString();
    }

    public static Database getDatabaseFromInputStream(InputStream jsonFileInputStream) {
        Database db;
        try {
            db = generateDatabaseFromInputStream(jsonFileInputStream);
        } catch (JSONException e) {
            e.printStackTrace();
            db = new Database();
        } catch (IOException e) {
            e.printStackTrace();
            db = new Database();
        }
        return db;
    }

    private static Database generateDatabaseFromInputStream(InputStream jsonFileInputStream)
            throws JSONException, IOException {
        JSONObject dbJsonRawObject = new JSONObject(readInputStreamToString(jsonFileInputStream));

        JSONArray sizesJsonArr = dbJsonRawObject.getJSONArray("sizes");
        List<Size> sizes = new LinkedList<>();
        JSONArray toppingsJsonArr = dbJsonRawObject.getJSONArray("toppings");
        List<Topping> toppings = new LinkedList<>();
        JSONArray crustsJsonArr = dbJsonRawObject.getJSONArray("crusts");
        List<Crust> crusts = new LinkedList<>();
        for (int i = 0; i < sizesJsonArr.length(); i++) {
            String name = sizesJsonArr.getJSONObject(i).getString("name");
            double price = sizesJsonArr.getJSONObject(i).getDouble("price");
            double dimension = sizesJsonArr.getJSONObject(i).getDouble("dimension");
            Size size = new Size(name, price, dimension);
            sizes.add(size);
        }
        for (int i = 0; i < crustsJsonArr.length(); i++) {
            String name = crustsJsonArr.getJSONObject(i).getString("name");
            double price = crustsJsonArr.getJSONObject(i).getDouble("price");
            Crust crust = new Crust(name, price);
            crusts.add(crust);
        }
        for (int i = 0; i < toppingsJsonArr.length(); i++) {
            String name = toppingsJsonArr.getJSONObject(i).getString("name");
            double price = toppingsJsonArr.getJSONObject(i).getDouble("price");
            String imageSource = toppingsJsonArr.getJSONObject(i).getString("image");
            Topping topping = new Topping(name, price, imageSource);
            toppings.add(topping);
        }
        return new Database(sizes, toppings, crusts);
    }
}

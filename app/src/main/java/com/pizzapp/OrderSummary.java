package com.pizzapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.DoesNotExist;

import java.util.LinkedList;
import java.util.List;


public class OrderSummary extends AppCompatActivity {

    private static final String LOG_TAG = OrderSummary.class.getSimpleName();
    LayoutInflater inflater;
    //    private LinearLayout mPizzaLayout;
    private TableLayout mPizzaLayout;
    private Button mDelivery;
    private Button mPickup;
    private Order finalOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        initializeDataMembers();
        Intent intent = getIntent();
        // todo: change heading here form hello to Order Summary
        finalOrder = (Order) intent.getSerializableExtra("order");

        if (finalOrder != null) {
            double testOrderTotalPrice = finalOrder.getTotalPrice();
            viewOrder(finalOrder);
            setPriceTextView(testOrderTotalPrice);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(this, intent);
        return true;
    }

    private void initializeDataMembers() {
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPizzaLayout = findViewById(R.id.table_of_pizzas);
        mPickup = findViewById(R.id.pickup_button);
        mDelivery = findViewById(R.id.delivery_button);
    }

    private void setPriceTextView(double testOrderTotalPrice) {
        TextView orderPrice = findViewById(R.id.order_total_price);
        orderPrice.setText("Order Total: " + testOrderTotalPrice + "$");
    }

    private void viewOrder(Order testOrder) {
        try {
            for (int i = 0; i < testOrder.getNumberOfPizzas(); i++) {
                Log.d(LOG_TAG, "There is a pizza to order");
                TableRow pizzaRow = getPizzaRow(testOrder.getPizza(i));
                mPizzaLayout.addView(pizzaRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TableRow getPizzaRow(Pizza pizza) {
        TableRow row = getTableRow();
        RelativeLayout pizzaDrawing = getPizzaDrawingLayout(pizza); // table layout of two rows. add toppings
        LinearLayout pizzaText = getPizzaTextLayout();
        pizzaText.addView(getPizzaHeadlineLayout());
        addPizzaDetails(pizza, pizzaText);

        row.addView(pizzaDrawing);
        row.addView(pizzaText);
        return row;
    }

    private RelativeLayout getPizzaDrawingLayout(Pizza pizza) {
        RelativeLayout pizzaDrawing = new RelativeLayout(this);
        pizzaDrawing.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        TableLayout onlyPizza = getPizzaTableLayout();
        for (int part_i = 0; part_i < 4; part_i += 2){
            TableRow rowOfTwoParts = getTableRow();
            FrameLayout drawnPartLeft = drawPart(pizza.getPizzaPart(part_i));
            rowOfTwoParts.addView(drawnPartLeft);
            FrameLayout drawnPartRight = drawPart(pizza.getPizzaPart(part_i + 1));
            rowOfTwoParts.addView(drawnPartRight);
            onlyPizza.addView(rowOfTwoParts);
        }
        addChangeButton(pizzaDrawing);
        return pizzaDrawing;
    }

    private void addChangeButton(RelativeLayout pizzaDrawing) {
        Button myButton = (Button)inflater.inflate(R.layout.change_button_layout, null);
        pizzaDrawing.addView(myButton);
        // todo: NOT CRUCIAL: add here a specific on click for a specific pizza to load in main
    }


    public FrameLayout drawPart(PizzaPart part){
        try{
            FrameLayout frame = getPizzaPartFrameLayout();
            ImageView partImage = getPizzaPartImage(part.getId());
            // todo: add the topping on top of the image
            frame.addView(partImage);
            return frame;
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return null;
        }
    }

    private ImageView getPizzaPartImage(int part_i) throws DoesNotExist{
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.ic_pizza_slice_color);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(R.dimen.recieptPizzaSize,
                R.dimen.recieptPizzaSize);
        switch (part_i){
            case 0:
                params.gravity = Gravity.BOTTOM|Gravity.END;
                image.setLayoutParams(params);
                image.setRotation(270);
                break;
            case 1:
                params.gravity = Gravity.BOTTOM|Gravity.START;
                image.setLayoutParams(params);
                image.setRotation(0);
                break;
            case 2:
                params.gravity = Gravity.TOP|Gravity.END;
                image.setLayoutParams(params);
                image.setRotation(180);
                break;
            case 3:
                params.gravity = Gravity.TOP|Gravity.START;
                image.setLayoutParams(params);
                image.setRotation(90);
                break;
            default:
                throw new DoesNotExist();
        }
        return image;
    }


    public FrameLayout getPizzaPartFrameLayout(){
        FrameLayout frame = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                R.dimen.recieptPizzaSize,
                R.dimen.recieptPizzaSize);
        params.gravity = Gravity.CENTER;
        frame.setLayoutParams(params);
        frame.setPadding(1,1,1,1);
        return frame;
    }

    private TableLayout getPizzaTableLayout() {
        TableLayout tableLayout = new TableLayout(this);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                                            TableLayout.LayoutParams.MATCH_PARENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        tableLayout.setLayoutParams(params);
        tableLayout.setPadding(15,15,15,15);
        return  tableLayout;
    }

    private TableRow getTableRow() {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        return row;
    }


    private LinearLayout getPizzaTextLayout() {
        LinearLayout genLayout = new LinearLayout(this);
        genLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5, 5, 5);
        genLayout.setLayoutParams(params);
        genLayout.setPadding(1, 1, 1, 1);
        return genLayout;
    }

    private LinearLayout getPizzaHeadlineLayout() {
        LinearLayout genLayout = new LinearLayout(this);
        genLayout.setOrientation(LinearLayout.HORIZONTAL);
        genLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return genLayout;
    }

    private void addPizzaDetails(Pizza pizza, LinearLayout pizzaText) {
        int numOfParts = pizza.getNumberOfParts();
        List<TextView> views = new LinkedList<>();
        views.addAll(getAllPartsAndToppings(pizza, numOfParts));
        views.add(getPizzaPriceView(pizza, numOfParts));
        for (TextView view : views)
            pizzaText.addView(view);
    }


    public TextView getAnonymousTextView() {
        TextView textView = new TextView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 0, 0);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    private TextView getPartLineView(int part_i, int numOfParts) {
        TextView partLineView = getAnonymousTextView();
        partLineView.setTextSize(13);
        partLineView.setText("Part " + (part_i + 1) + "/" + numOfParts + ":");
        partLineView.setVisibility(View.VISIBLE);
        return partLineView;
    }

    private TextView getToppingView(Topping topping) {
        TextView toppingsView = getAnonymousTextView();
        toppingsView.setTextSize(8);
        toppingsView.setText(topping.getName() + "..." + topping.getPrice());
        toppingsView.setVisibility(View.VISIBLE);
        return toppingsView;
    }

    private List<TextView> getPartToppings(PizzaPart currPart) {
        List<TextView> toppings = new LinkedList<>();
        for (Topping topping : currPart.getToppings()) {
            toppings.add(getToppingView(topping));
        }
        return toppings;
    }

    private List<TextView> getPartAndToppingsViews(int i, Pizza pizza) {
        List<TextView> partAndTopping = new LinkedList<>();
        int numOfParts = pizza.getNumberOfParts();
        PizzaPart currPart = pizza.getPizzaPart(i);

        TextView pizzaPartLineView = getPartLineView(i, numOfParts);
        List<TextView> toppingsViews = getPartToppings(currPart);

        partAndTopping.add(pizzaPartLineView);
        partAndTopping.addAll(toppingsViews);
        return partAndTopping;
    }

    private List<TextView> getAllPartsAndToppings(Pizza pizza, int numOfParts) {
        List<TextView> partsAndToppings = new LinkedList<>();
        for (int i = 0; i < numOfParts; i++) {
            List<TextView> partAndTopping = getPartAndToppingsViews(i, pizza);
            partsAndToppings.addAll(partAndTopping);
        }
        return partsAndToppings;
    }

    private TextView getPizzaPriceView(Pizza pizza, int numOfParts) {
        double price = pizza.getPrice();
        TextView priceView = getAnonymousTextView();
        priceView.setText("Total:........." + price + "$");
        priceView.setTextSize(15);
        priceView.setTypeface(null, Typeface.BOLD);
        priceView.setVisibility(View.VISIBLE);
        return priceView;
    }


    public void launchDeliveryActivity(View view) {
        Log.d(LOG_TAG, "Delivery button clicked!");
        mDelivery.setTextColor(Color.RED);
        Intent intent = new Intent(this, Delivery.class);
        startActivity(intent);
    }


    public void launchPickupActivity(View view) {
        Log.d(LOG_TAG, "Pickup button clicked!");
        mPickup.setTextColor(Color.RED);
        Intent intent = new Intent(this, Pickup.class);
        startActivity(intent);
    }

    public void backToMain(View view) {
        Log.d(LOG_TAG, "backToMain");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("pizza", finalOrder.getLastPizza());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        mDelivery.setTextColor(Color.BLACK);
        mPickup.setTextColor(Color.BLACK);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }


}

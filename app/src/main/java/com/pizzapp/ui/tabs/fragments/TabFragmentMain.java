package com.pizzapp.ui.tabs.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.pizzapp.MainActivity;
import com.pizzapp.OrderSummary;
import com.pizzapp.R;
import com.pizzapp.ToppingsPopUp;
import com.pizzapp.model.Order;
import com.pizzapp.model.pizza.Pizza;
import com.pizzapp.model.pizza.PizzaPart;
import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.DoesNotExist;
import com.pizzapp.utilities.StaticFunctions;
import com.pizzapp.utilities.UI.PizzaPartImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TabFragmentMain extends Fragment implements Serializable {

    private static final String LOG_TAG = TabFragmentMain.class.getSimpleName();
    private static final int TOPPING_CHOOSING_RESULT = 1;
    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;

    private Pizza currentPizza;
    private Order finalOrder;
    private List<PizzaPartImage> partImages = new ArrayList<>();
    private List<ImageView> toppingImages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_main, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializePizzaPartImageList(view);
        initiateCurrentPizzaOrder();
        showPrice(view);
        addButtonListeners(view);
        createCurrentPizza();
        MainActivity.updatePizzaDimensionsIndicators(getActivity(), currentPizza);
    }


    private void initiateCurrentPizzaOrder() {
        finalOrder = ((MainActivity) this.getActivity()).order;
        currentPizza = finalOrder.getLastPizza();
    }

    private void initializePizzaPartImageList(View view) {
        partImages.add(new PizzaPartImage(R.id.topRightFrame, 0, R.id.topRight, "topRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomRightFrame, 1, R.id.bottomRight, "bottomRight", view));
        partImages.add(new PizzaPartImage(R.id.bottomLeftFrame, 2, R.id.bottomLeft, "bottomLeft", view));
        partImages.add(new PizzaPartImage(R.id.topLeftFrame, 3, R.id.topLeft, "topLeft", view));
    }

    private void addContinueOnClickListener(View view) {
        Button placeOrderButton = view.findViewById(R.id.placeOrder);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderSummary.class);
                intent.putExtra("order", finalOrder);
                startActivity(intent);
            }
        });
    }

    private void addButtonListeners(View view) {
        ImageView topRightSlice = view.findViewById(R.id.topRight);
        ImageView bottomRightSlice = view.findViewById(R.id.bottomRight);
        ImageView bottomLeftSlice = view.findViewById(R.id.bottomLeft);
        ImageView topLeftSlice = view.findViewById(R.id.topLeft);
        List<ImageView> slices = Arrays.asList(topRightSlice, bottomRightSlice, bottomLeftSlice, topLeftSlice);
        addOnClickListener(slices);
        addClearButtonOnClickListener(view);
        addContinueOnClickListener(view);
    }

    private void showTextChange(final TextView price) {
        new CountDownTimer(250, 1000) {

            public void onTick(long millisUntilFinished) {
                price.setTextColor(Color.RED);
            }

            public void onFinish() {
                price.setTextColor(Color.BLACK);
            }
        }.start();
    }

    private void addClearButtonOnClickListener(final View view) {
        Button clearButton = view.findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PizzaPartImage pizzaPartImage : partImages) {
                    pizzaPartImage.removeAllTopping();
                }
                currentPizza.removePizzaToppings();
                finalOrder.upadateLastPizza(currentPizza);
                showPrice(view);
                showTextChange((TextView) view.findViewById(R.id.orderPrice));
            }
        });
    }

    private void showPrice(View view) {
        TextView price = view.findViewById(R.id.orderPrice);
        price.setTextColor(Color.BLACK);
        showUpdatedPrice();
    }

    public void showUpdatedPrice() {
        View view = getView();
        TextView price = view.findViewById(R.id.orderPrice);
        String priceDisplay = getResources().getString(R.string.price_showing_prefix) + finalOrder.getTotalPrice() + getResources().getString(R.string.price_showing_suffix);
        price.setText(priceDisplay);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createCurrentPizza() {
        for (int i = 0; i < currentPizza.getNumberOfParts(); i++) {
            for (Topping topping : currentPizza.getParts().get(i).getToppings()) {
                partImages.get(i).addTopping(topping, true);
            }
        }
    }

    private void addOnClickListener(List<ImageView> slices) {
        for (ImageView slice : slices) {
            slice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openPopup(getPartClicked(v.getId()));
                    } catch (DoesNotExist doesNotExist) {
                        showErrorMessage(v);
                    }
                }
            });
        }
    }

    private int getPartClicked(int id) throws DoesNotExist {
        switch (id) {
            case (R.id.topRight):
                return TOP_RIGHT_SLICE;
            case (R.id.bottomRight):
                return BOTTOM_RIGHT_SLICE;
            case (R.id.bottomLeft):
                return BOTTOM_LEFT_SLICE;
            case (R.id.topLeft):
                return TOP_LEFT_SLICE;
        }
        throw new DoesNotExist();
    }

    private void showErrorMessage(View view) {
        Toast toast = Toast.makeText(view.getContext(), R.string.error_message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void openPopup(int id) {
        Intent intent = new Intent(getActivity(), ToppingsPopUp.class);
        intent.putExtra("callingId", id);
        intent.putExtra("pizza", currentPizza);
        intent.putExtra("order", finalOrder);
        startActivityForResult(intent, TOPPING_CHOOSING_RESULT);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "return from popup with result: " + requestCode);
        if (requestCode == TOPPING_CHOOSING_RESULT) {
            currentPizza = (Pizza) data.getSerializableExtra("pizza");
            finalOrder.upadateLastPizza(currentPizza);
            ((MainActivity) this.getActivity()).order = finalOrder;
            for (ImageView toppingImage : toppingImages) {
                toppingImage.setVisibility(View.GONE);
            }
            toppingImages.clear();
            createCurrentPizza();
            showUpdatedPrice();
        }
    }
}

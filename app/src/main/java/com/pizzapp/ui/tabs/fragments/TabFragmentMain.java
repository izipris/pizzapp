package com.pizzapp.ui.tabs.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TabFragmentMain extends Fragment implements Serializable {

    public static final int TOPPING_CHOOSING_RESULT = 1;
    private static final int TOP_RIGHT_SLICE = 0;
    private static final int BOTTOM_RIGHT_SLICE = 1;
    private static final int BOTTOM_LEFT_SLICE = 2;
    private static final int TOP_LEFT_SLICE = 3;
    private static final int PIZZA_PASSED = 3;
    private static final int PIZZA_NOT_PASSED = 2;
    private static final int HEIGHT = 76;
    private static final int WIDTH = 76;
    private static final int ANGLE_TO_ROTATE = 90;

    private Pizza currentPizza;
    private Order finalOrder;
    private List<ImageView> toppingImages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateCurrentPizzaOrder();
        showNumberOfPizzas(view);
        showPrice(view);
        addButtonListeners(view);
        createCurrentPizza(view);
    }

    private void initiateCurrentPizzaOrder() {
        finalOrder = ((MainActivity) this.getActivity()).order;
        currentPizza = finalOrder.getLastPizza();
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
        addAddButtonOnClickListener(view);
        addContinueOnClickListener(view);
    }

    private void addAddButtonOnClickListener(final View view) {
        Button addButton = view.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCurrentPizza();
                finalOrder.addPizza(currentPizza);
                showPrice(view);
                showNumberOfPizzas(view);
                final TextView price = view.findViewById(R.id.orderPrice);
                showTextChange(price);
            }
        });
    }

    private void copyCurrentPizza() {
        Pizza tempPizza = currentPizza;
        currentPizza = new Pizza(currentPizza.getNumberOfParts(),
                currentPizza.getSize(), currentPizza.getCrust());
        List<PizzaPart> partList = tempPizza.getParts();
        for (int i=0; i < partList.size(); i++){
            List<Topping> partToppings = tempPizza.getParts().get(i).getToppings();
            if (partToppings.size() > 0){
                for (int j=0; j<partToppings.size(); j++){
                    currentPizza.getParts().get(i).addTopping(partToppings.get(j));
                }
            }
        }
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
                for (ImageView toppingImage : toppingImages) {
                    toppingImage.setVisibility(View.GONE);
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

    public void showUpdatedPrice(){
        View view = getView();
        TextView price = view.findViewById(R.id.orderPrice);
        String priceDisplay = getResources().getString(R.string.price_showing_prefix) + finalOrder.getTotalPrice() + getResources().getString(R.string.price_showing_suffix);
        price.setText(priceDisplay);
    }

    private void showNumberOfPizzas(View view) {
        TextView numberOfPizzas = view.findViewById(R.id.number_of_pizzas);
        showTextChange(numberOfPizzas);
        numberOfPizzas.setText(String.valueOf(finalOrder.getNumberOfPizzas()));
    }


    private void createCurrentPizza(View view) {
        int currentPart = 0;
        if (currentPizza != null) {
            for (PizzaPart pizzaPart : currentPizza.getParts()) {
                for (Topping topping : pizzaPart.getToppings()) {
                    addTopping(view, currentPart, topping);
                }
                currentPart++;
            }
        }
    }

    private void addTopping(View view, final int currentPart, Topping topping) {
        try {
            FrameLayout frameLayout = getAppropriateFrameId(currentPart, view);
            ImageView newTopping = new ImageView(getActivity());
            newTopping.setImageDrawable(convertStringToDrawable(topping.getImageSource()));
            newTopping.setRotation(getCurrentRotation(currentPart));
            newTopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPopup(currentPart);
                }
            });
            FrameLayout.LayoutParams layoutParams = new
                    FrameLayout.LayoutParams(StaticFunctions.convertDpToPx(154), StaticFunctions.convertDpToPx(154));
            setGravity(layoutParams, currentPart);
            layoutParams.height = StaticFunctions.convertDpToPx(HEIGHT);
            layoutParams.width = StaticFunctions.convertDpToPx(WIDTH);

            newTopping.setLayoutParams(layoutParams);
            toppingImages.add(newTopping);
            frameLayout.addView(newTopping);
        } catch (DoesNotExist doesNotExist) {
            showErrorMessage(view);
        }
    }

    private FrameLayout getAppropriateFrameId(int sliceId, View view) throws DoesNotExist {
        switch (sliceId) {
            case (TOP_RIGHT_SLICE):
                return view.findViewById(R.id.topRightFrame);
            case (BOTTOM_RIGHT_SLICE):
                return view.findViewById(R.id.bottomRightFrame);
            case (BOTTOM_LEFT_SLICE):
                return view.findViewById(R.id.bottomLeftFrame);
            case (TOP_LEFT_SLICE):
                return view.findViewById(R.id.topLeftFrame);
        }
        throw new DoesNotExist();

    }

    private void setGravity(FrameLayout.LayoutParams layoutParams, int currentPart) {
        switch (currentPart) {
            case (TOP_RIGHT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
                break;
            case (BOTTOM_RIGHT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.START;
                break;
            case (BOTTOM_LEFT_SLICE):
                layoutParams.gravity = Gravity.TOP | Gravity.END;
                break;
            case (TOP_LEFT_SLICE):
                layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                break;
        }
    }

    private int getCurrentRotation(int currentPart) {
        return ANGLE_TO_ROTATE * currentPart;
    }

    private Drawable convertStringToDrawable(String name) {
        int id = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
        return getResources().getDrawable(id);
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
        int numberOfExtrasPassed;
        intent.putExtra("callingId", id);
        if (currentPizza != null) {
            numberOfExtrasPassed = PIZZA_PASSED;
            intent.putExtra("pizza", currentPizza);
            intent.putExtra("order", finalOrder);
        } else {
            numberOfExtrasPassed = PIZZA_NOT_PASSED;
        }
        intent.putExtra("numberOfExtras", numberOfExtrasPassed);
        startActivityForResult(intent, TOPPING_CHOOSING_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOPPING_CHOOSING_RESULT){
            currentPizza = (Pizza) data.getSerializableExtra("pizza");
            finalOrder.upadateLastPizza(currentPizza);
            ((MainActivity) this.getActivity()).order = finalOrder;
            for (ImageView toppingImage : toppingImages) {
                toppingImage.setVisibility(View.GONE);
            }
            toppingImages.clear();
            createCurrentPizza(getView());
            showUpdatedPrice();
        }
    }
}

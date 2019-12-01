package com.pizzapp.model.pizza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PizzaPart implements Serializable {
    private List<Topping> toppings;
    private List<String> toppingNames;
    private boolean hasTopping;
    private int id;

    public PizzaPart(int id) {
        toppings = new ArrayList<Topping>();
        toppingNames = new ArrayList<String>();
        hasTopping = false;
        this.id = id;
    }

    public boolean isHasTopping() {
        return hasTopping;
    }

    public boolean hasCertainTopping(String toppingName) {
        if (toppingNames.contains(toppingName)) {
            return true;
        }
        return false;
    }

    public void addTopping(Topping topping) {
        this.toppings.add(topping);
        this.toppingNames.add(topping.getName());
        hasTopping = true;
    }

    public void removeTopping(Topping topping) {
        if (hasTopping) {
            toppings.remove(topping);
            toppingNames.remove(topping.getName());
        }
        if (toppings.size() == 0) {
            hasTopping = false;
        }
    }

    public void removeAllToppings() {
        toppingNames.clear();
        toppings.clear();
        hasTopping = false;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public int getId() {
        return id;
    }
}

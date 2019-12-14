package com.pizzapp.model.pizza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a POJO which represents a slice of a pizza
 */
public class PizzaPart implements Serializable {
    private List<Topping> toppings;
    private boolean hasTopping;
    private int id;

    public PizzaPart(int id) {
        toppings = new ArrayList<>();
        hasTopping = false;
        this.id = id;
    }

    public boolean isHasTopping() {
        return hasTopping;
    }

    public boolean hasCertainTopping(Topping topping) {
        for (Topping partTopping : toppings) {
            if (partTopping.getName().equals(topping.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addTopping(Topping topping) {
        this.toppings.add(topping);
        hasTopping = true;
    }

    public void removeTopping(Topping topping) {
        if (hasTopping) {
            Topping toppingToRemove = null;
            for (Topping toppingObject : toppings) {
                if (toppingObject.getName().equals(topping.getName())) {
                    toppingToRemove = toppingObject;
                }
            }
            if (toppingToRemove != null) {
                toppings.remove(toppingToRemove);
            }
        }
        if (toppings.size() == 0) {
            hasTopping = false;
        }
    }

    public void removeAllToppings() {
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

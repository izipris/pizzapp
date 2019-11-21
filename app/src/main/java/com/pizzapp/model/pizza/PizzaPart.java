package com.pizzapp.model.pizza;

import java.util.ArrayList;
import java.util.List;

public class PizzaPart {
    private List<Topping> toppings;
    private boolean hasTopping;
    private int id;

    public PizzaPart(int id){
        toppings = new ArrayList<Topping>();
        hasTopping = false;
        this.id = id;
    }

    public boolean isHasTopping() {
        return hasTopping;
    }

    public boolean hasCertainTopping(Topping topping){
        if (toppings.contains(topping)){
            return true;
        }
        return false;
    }

    public void addTopping(Topping topping) {
        this.toppings.add(topping);
        hasTopping = true;
    }

    public void removeTopping(Topping topping){
        if (hasTopping) {
            toppings.remove(topping);
        }
        if (toppings.size() == 0) {
            hasTopping = false;
        }
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public int getId() {
        return id;
    }
}

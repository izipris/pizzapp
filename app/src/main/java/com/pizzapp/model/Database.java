package com.pizzapp.model;

import com.pizzapp.model.pizza.Crust;
import com.pizzapp.model.pizza.Size;
import com.pizzapp.model.pizza.Topping;

import java.util.List;

public class Database {
    private List<Size> sizes;
    private List<Topping> toppings;
    private List<Crust> crusts;

    public Database(){}

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public List<Crust> getCrusts() {
        return crusts;
    }

    public void setCrusts(List<Crust> crusts) {
        this.crusts = crusts;
    }
}

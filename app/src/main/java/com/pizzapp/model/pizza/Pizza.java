package com.pizzapp.model.pizza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/**
 * This class represent a pizza in an order.
 */
public class Pizza {
    private Size size;
    private Crust crust;
    private CheeseType cheeseType;
    private List<PizzaPart> parts;
    private double price;

    public Pizza(Size size, Crust crust, CheeseType cheeseType){
        this.size = size;
        this.crust = crust;
        this.cheeseType = cheeseType;
        parts = new Vector<>();
        parts.add(new PizzaPart());
        calculatePrice();
    }

    /**
     * copy constructor if the user want to duplicate pizza.
     * @param pizza another pizza.
     */
    public Pizza(Pizza pizza){
        this.size = pizza.size;
        this.crust = pizza.crust;
        this.cheeseType = pizza.cheeseType;
        this.parts = pizza.parts;
        this.price = pizza.price;
    }

    private void calculatePrice(){
        price = size.getPrice() + crust.getPrice() + cheeseType.getPrice();
        for (PizzaPart part: parts){
            if (part.isHasTopping()){
                price += part.getTopping().getPrice() / (double) parts.size();
            }
        }
    }

    public double getPrice() {
        return price;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Crust getCrust() {
        return crust;
    }

    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    public CheeseType getCheeseType() {
        return cheeseType;
    }

    public void setCheeseType(CheeseType cheeseType) {
        this.cheeseType = cheeseType;
    }
}

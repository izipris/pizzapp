package com.pizzapp.model;

import com.pizzapp.model.pizza.Pizza;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * This class represent one order of pizzas
 */
public class Order implements Serializable {
    private int id;
    private List<Pizza> pizzas;

    public Order(int id){
        this.id = id;
        pizzas = new Vector<>();
    }

    public void addPizza(Pizza pizza){
        pizzas.add(pizza);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Pizza pizza: pizzas){
            totalPrice += pizza.getPrice();
        }
        return totalPrice;
    }

    public Pizza getPizza(int index) {
        return pizzas.get(index);
    }

    public void upadateLastPizza(Pizza newPizza){
        pizzas.set(pizzas.size() - 1, newPizza);
    }

    public int getNumberOfPizzas(){return pizzas.size();}

    public void upadateLastPizza(Pizza newPizza){
        pizzas.set(pizzas.size() - 1, newPizza);
    }
}

package com.pizzapp;

import com.pizzapp.model.pizza.Pizza;

public class Order {

    public Order(int id) {
        this.id = id;
        this.price = 0;
    }

    private int id;
    private Pizza pizza;
    private int price;


    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}

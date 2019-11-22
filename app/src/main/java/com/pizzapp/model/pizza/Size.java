package com.pizzapp.model.pizza;

import java.io.Serializable;

public class Size implements Serializable {
    private String name;
    private double price;

    public Size() {
        this.name = "";
        this.price = 0;
    }

    public Size(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

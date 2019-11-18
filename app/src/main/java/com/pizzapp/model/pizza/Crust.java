package com.pizzapp.model.pizza;

public class Crust {
    private String name;
    private double price;

    public Crust() {
        this.name = "";
        this.price = 0;
    }

    public Crust(String name, double price) {
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

package com.pizzapp.model.pizza;

public class Size {
    private String name;
    private float price;

    public Size() {
        this.name = "";
        this.price = 0;
    }

    public Size(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

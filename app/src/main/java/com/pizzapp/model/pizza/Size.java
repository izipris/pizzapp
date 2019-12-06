package com.pizzapp.model.pizza;

import java.io.Serializable;

public class Size implements Serializable {
    private String name;
    private double price;
    private double dimension;
    private String caption;

    public Size() {
        this.name = "";
        this.price = 0;
    }

    public Size(String name, double price, double dimension, String caption) {
        this.name = name;
        this.price = price;
        this.dimension = dimension;
        this.caption = caption;
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

    public double getDimension() {
        return dimension;
    }

    public void setDimension(double dimension) {
        this.dimension = dimension;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

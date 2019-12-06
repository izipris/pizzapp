package com.pizzapp.model.pizza;

import java.io.Serializable;

public class Topping implements Serializable {
    private String name;
    private double price;
    private String imageSource;
    private String iconSource;

    public Topping() {
        this.name = "";
        this.price = 0;
        this.imageSource = "";
    }

    public Topping(String name, double price, String imageSource, String iconSource) {
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
        this.iconSource = iconSource;
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

    public String getImageSource() {
        return imageSource;
    }

    public String getIconSource(){
        return iconSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}

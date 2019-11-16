package com.pizzapp.model.pizza;

public class Topping {
    private String name;
    private float price;
    private String imageSource;

    public Topping() {
        this.name = "";
        this.price = 0;
        this.imageSource = "";
    }

    public Topping(String name, float price, String imageSource) {
        this.name = name;
        this.price = price;
        this.imageSource = imageSource;
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

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}

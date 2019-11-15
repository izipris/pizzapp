
package com.pizzapp;

import java.util.List;

public class Order {

    public Order(int mId) {
        this.mId = mId;
        this.mPizzas.add(new Pizza(1));
        this.mPrice = 0.0;
    }

    private int mId;

    private List<Pizza> mPizzas;

    private Double mPrice;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public List<Pizza> getPizzas() {
        return mPizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        mPizzas = pizzas;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

}

package com.pizzapp.model.pizza;

public class PizzaPart {
    private Topping topping;
    private boolean hasTopping;

    public PizzaPart(){
        topping = null;
        hasTopping = false;
    }

    public PizzaPart(Topping topping){
        this.topping = topping;
        hasTopping = true;
    }

    public boolean isHasTopping() {
        return hasTopping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
        hasTopping = true;
    }

    public void removeTopping(){
        topping = null;
        hasTopping = false;
    }

    public Topping getTopping() {
        return topping;
    }
}

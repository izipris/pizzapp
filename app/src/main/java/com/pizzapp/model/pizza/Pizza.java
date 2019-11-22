package com.pizzapp.model.pizza;


import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * This class represent a pizza in an order.
 */
public class Pizza implements Serializable {
    private static final int DEFAULT_NUMBER_OF_SLICES = 1;
    private static final String OUT_OF_BOUNDS_MSG = "There no pizza part in the given index.";

    private int numberOfSlices;
    private Size size;
    private Crust crust;
    private List<PizzaPart> parts;

    public Pizza(int numberOfSlices, Size size, Crust crust){
        this.numberOfSlices = numberOfSlices;
        this.size = size;
        this.crust = crust;
        parts = new Vector<>();
        for(int i = 0; i < numberOfSlices; ++i) {
            parts.add(new PizzaPart(i));
        }
    }

    /**
     * copy constructor if the user want to duplicate pizza.
     * @param pizza another pizza.
     */
    public Pizza(Pizza pizza){
        this.numberOfSlices = pizza.numberOfSlices;
        this.size = pizza.size;
        this.crust = pizza.crust;
        this.parts = pizza.parts;
    }

    public Pizza(Size size, Crust crust){
        this(DEFAULT_NUMBER_OF_SLICES, size, crust);
    }

    public double getPrice(){
        double price = size.getPrice() + crust.getPrice();
        for (PizzaPart part: parts){
            if (part.isHasTopping()){
                for (Topping topping:part.getToppings())
                price += topping.getPrice() / (double) numberOfSlices;
            }
        }
        return price;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Crust getCrust() {
        return crust;
    }

    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    public PizzaPart getPizzaPart(int index) throws IndexOutOfBoundsException{
        if (index < 0 || index > numberOfSlices){
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MSG);
        }
        return parts.get(index);
    }

    public List<PizzaPart> getParts(){
        return parts;
    }
}

package com.pizzapp.model.pizza;


/**
 * This class represent a pizza in an order.
 */
public class Pizza {
    private static final int DEFAULT_NUMBER_OF_SLICES = 1;
    private static final String OUT_OF_BOUNDS_MSG = "There no pizza part in the given index.";

    private int numberOfSlices;
    private Size size;
    private Crust crust;
    private CheeseType cheeseType;
    private PizzaPart[] parts;
    private double price;

    public Pizza(int numberOfSlices, Size size, Crust crust, CheeseType cheeseType){
        this.numberOfSlices = numberOfSlices;
        this.size = size;
        this.crust = crust;
        this.cheeseType = cheeseType;
        parts = new PizzaPart[numberOfSlices];
        for(int i = 0; i < numberOfSlices; ++i) {
            parts[i] = new PizzaPart();
        }
        calculatePrice();
    }

    /**
     * copy constructor if the user want to duplicate pizza.
     * @param pizza another pizza.
     */
    public Pizza(Pizza pizza){
        this.numberOfSlices = pizza.numberOfSlices;
        this.size = pizza.size;
        this.crust = pizza.crust;
        this.cheeseType = pizza.cheeseType;
        this.parts = pizza.parts;
        this.price = pizza.price;
    }

    public Pizza(Size size, Crust crust, CheeseType cheeseType){
        this(DEFAULT_NUMBER_OF_SLICES, size, crust, cheeseType);
    }

    private void calculatePrice(){
        price = size.getPrice() + crust.getPrice() + cheeseType.getPrice();
        for (PizzaPart part: parts){
            if (part.isHasTopping()){
                price += part.getTopping().getPrice() / (double) numberOfSlices;
            }
        }
    }

    public double getPrice() {
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

    public CheeseType getCheeseType() {
        return cheeseType;
    }

    public void setCheeseType(CheeseType cheeseType) {
        this.cheeseType = cheeseType;
    }

    public PizzaPart getPizzaPart(int index) throws IndexOutOfBoundsException{
        if (index < 0 || index > numberOfSlices){
            throw new IndexOutOfBoundsException(OUT_OF_BOUNDS_MSG);
        }
        return parts[index];
    }
}

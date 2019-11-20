
package com.pizzapp;



@SuppressWarnings("unused")
public class Part {

    public Part(int mId, String toppings){
        this.mId = mId;
        this.mToppings = toppings;
    }

    @Override
    public String toString() {
        return "Part{" +
                "mId=" + mId +
                ", mToppings='" + mToppings + '\'' +
                '}';
    }

    private int mId;

    private String mToppings;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getToppings() {
        return mToppings;
    }

    public void setToppings(String toppings) {
        mToppings = toppings;
    }

}

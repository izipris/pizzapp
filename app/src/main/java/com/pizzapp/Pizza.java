
package com.pizzapp;

import java.util.List;
//import javax.annotation.Generated;
//import com.google.gson.annotations.SerializedName;

//@Generated("net.hexar.json2pojo")
//@SuppressWarnings("unused")
public class Pizza {

    public Pizza(int mId) {
        this.mId = mId;
        this.mCheesType = "reg";
        this.mParts.add(new Part(1, "mushrooms" ));
        this.mSize = "L";
        this.mThickness = "EXXTRA THICC";
    }

    private int mId;

    private String mCheesType;

    private List<Part> mParts;

    private String mSize;

    private String mThickness;

    public String getCheesType() {
        return mCheesType;
    }

    public void setCheesType(String cheesType) {
        mCheesType = cheesType;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public List<Part> getParts() {
        return mParts;
    }

    public void setParts(List<Part> parts) {
        mParts = parts;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getThickness() {
        return mThickness;
    }

    public void setThickness(String thickness) {
        mThickness = thickness;
    }

}

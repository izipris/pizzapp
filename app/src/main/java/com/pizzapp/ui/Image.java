package com.pizzapp.ui;

import android.view.View;

public class Image {
    protected int id;
    protected int pizzaPart;
    protected String name;
    protected View view;

    public Image() {
        id = -1;
        pizzaPart = -1;
        name = "";
        this.view = null;
    }

    public int getId() {
        return id;
    }

    public int getPizzaPart() {
        return pizzaPart;
    }

    public String getName() {
        return name;
    }

}

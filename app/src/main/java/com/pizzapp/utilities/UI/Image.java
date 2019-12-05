package com.pizzapp.utilities.UI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pizzapp.utilities.StaticFunctions;

import java.util.ArrayList;
import java.util.List;

public class Image {
    protected int id;
    protected int pizzaPart;
    protected String name;
    protected View view;
    protected static List<Integer> imageIds = new ArrayList<>();

    public Image(){
        id = -1;
        pizzaPart = -1;
        name = "";
        this.view = null;
    }

    public int getId(){
        return id;
    }

    public int getPizzaPart(){
        return pizzaPart;
    }

    public String getName(){
        return name;
    }

    public List<Integer> getImageIds(){
        return imageIds;
    }

    public void addId(int id){
        imageIds.add(id);
    }

    public void removeId(int id){
        imageIds.remove(id);
    }

}

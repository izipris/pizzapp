package com.pizzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pizzapp.model.pizza.Topping;
import com.pizzapp.utilities.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.GridLayout.spec;

public class ToppingsPopUp extends AppCompatActivity {

    int TOP_RIGHT_SLICE = 0;
    int BOTTOM_RIGHT_SLICE = 1;
    int BOTTOM_LEFT_SLICE = 2;
    int TOP_LEFT_SLICE = 3;
    int BOX_WIDTH = 120;
    int BOX_HEIGHT = 52;
    int ENLARGED_WIDTH = 130;
    int ENLARGED_HEIGHT = 130;
    int ENLARGED_MARGIN_FROM_TOP_TOP_SLICES = 150;
    int ENLARGED_MARGIN_FROM_LEFT_LEFT_SLICES = 75;
    int ORIGINAL_WIDTH = 76;
    int ORIGINAL_HEIGHT = 76;
    int ORIGINAL_MARGIN_FROM_TOP_TOP_SLICES = 200;
    int ORIGINAL_MARGIN_FROM_TOP_BOTTOM_SLICES = 275;
    int ORIGINAL_MARGIN_FROM_LEFT_LEFT_SLICES = 130;
    int ORIGINAL_MARGIN_FROM_LEFT_RIGHT_SLICES = 207;


    private GridLayout gridLayout;
    int currentSliceId = -1;
    private List<Topping> toppingsList = new ArrayList<>();
    private Map<Integer, String> idMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_toppings);
        initiateIdMap();
        toppingsList = IO.getDatabaseFromInputStream(getResources().openRawResource(R.raw.database)).getToppings();
//        GridLayout layout = (GridLayout) findViewById(R.id.grid_layout);
//        layout.setRowCount(4);
//        int numberOfColumns = calculateNumberOfColumns();
//        layout.setColumnCount(numberOfColumns);
//        for (int i = 0; i < 4; i++) {
//            GridLayout.Spec rowSpec = GridLayout.spec(i, 1, 1);
//            for (int j = 0; j < numberOfColumns; j++) {
//                GridLayout.Spec colSpec = spec(j, 1, 1);
//                final CheckBox checkBox = new CheckBox(this);
//                checkBox.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
//                checkBox.setId(4 * j + i);
//                checkBox.setText(toppingsList.get(4 * j + i).getName());
//                checkBox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (checkBox.isChecked()){
//                            // TODO: 18 נובמבר 2019 add item picture and add to object
//                        }
//                        else {
//                            // TODO: 18 נובמבר 2019 delete from picture
//                        }
//                    }
//                });
//                GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams();
//                myGLP.rowSpec = rowSpec;
//                myGLP.columnSpec = colSpec;
//                layout.addView(checkBox, myGLP);
//            }
//        }
//    }
//
//
//    private int calculateNumberOfColumns() {
//        int numberOfRows = toppingsList.size() / 4;
//        if (toppingsList.size() % 4 != 0){
//            numberOfRows++;
//        }
//        return numberOfRows;
    }

//    private void addOnClickListener(List<ImageView> slices) {
//        for (ImageView slice: slices){
//            slice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), ToppingsPopUp.class);
//                    startActivity(intent);
//                }
//            });
//        }
//    }

    private void initiateIdMap(){
        addEntryToMap(R.id.topRight, "topRight");
        addEntryToMap(R.id.bottomRight, "bottomRight");
        addEntryToMap(R.id.bottomLeft, "bottomLeft");
        addEntryToMap(R.id.topLeft, "topLeft");
    }

    private void addEntryToMap(int value, String key){
        idMap.put(value, key);
    }

    private void removeEntryFromMap(String value){
        idMap.remove(findKeyFromValue(value));
    }

    private void setupToppingGrid(int sliceId){

    }

    private void shrinkSlice(int sliceId){
        changeSize(sliceId, false);
    }

    private void enlargeSlice(Integer sliceId){
        changeSize(sliceId, true);
    }

    private void changeSize(int sliceId, boolean enlarge){
        ImageView part = findViewById(sliceId);
        int topMargin, leftMargin;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) part.getLayoutParams();

            topMargin = calculateTopMargin(sliceId, enlarge);
            leftMargin = calculateLeftMargin(sliceId, enlarge);
            if (enlarge) {
                layoutParams.height = convertDpToPx(ENLARGED_HEIGHT);
                layoutParams.width = convertDpToPx(ENLARGED_WIDTH);
            } else {
                layoutParams.height = convertDpToPx(ORIGINAL_HEIGHT);
                layoutParams.width = convertDpToPx(ORIGINAL_WIDTH);
            }
        layoutParams.setMargins(leftMargin, topMargin, 0, 0);
        part.setLayoutParams(layoutParams);
    }

    private int calculateTopMargin(int sliceId, boolean enlarge){
        if (isSubstring(("top"), idMap.get(sliceId))) {
            if (enlarge) {
                return convertDpToPx(ENLARGED_MARGIN_FROM_TOP_TOP_SLICES);
            } else {
                return convertDpToPx(ORIGINAL_MARGIN_FROM_TOP_TOP_SLICES);
            }
        }
        return convertDpToPx(ORIGINAL_MARGIN_FROM_TOP_BOTTOM_SLICES);

    }

    private int calculateLeftMargin(int sliceId, boolean enlarge){
        if (isSubstring(("Left"), idMap.get(sliceId))) {
            if (enlarge) {
                return convertDpToPx(ENLARGED_MARGIN_FROM_LEFT_LEFT_SLICES);
            } else {
                return convertDpToPx(ORIGINAL_MARGIN_FROM_LEFT_LEFT_SLICES);
            }
        }
        return convertDpToPx(ORIGINAL_MARGIN_FROM_LEFT_RIGHT_SLICES);

    }



    private void addTopping(int sliceId, String topping){

    }

    private void removeTopping(int sliceId, String topping){

    }

    public void onClick(View view) {
        int newId = view.getId();
        if (newId != currentSliceId && currentSliceId != -1){
            shrinkSlice(currentSliceId);
        }
        enlargeSlice(newId);
        currentSliceId = newId;
    }

    private Integer findKeyFromValue(String value){
        for (Map.Entry<Integer, String> entry: idMap.entrySet()){
            if (entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return -1;
    }

    private Boolean isSubstring(String s1,String s2){
        for (int i = 0; i <= s2.length() - s1.length(); i++){
            int j;
            for (j =0; j < s1.length(); j++){
                if (s2.charAt(i + j) != s1.charAt(j)){
                    break;
                }
            }
            if (j == s1.length()){
                return true;
            }
        }
        return false;
    }

    private int convertDpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}

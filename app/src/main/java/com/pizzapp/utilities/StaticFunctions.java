package com.pizzapp.utilities;

import android.content.res.Resources;

import java.util.Random;

public class StaticFunctions {
    public static int convertDpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt() * random.nextInt();
    }
}

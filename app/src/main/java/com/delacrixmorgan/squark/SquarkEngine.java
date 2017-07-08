package com.delacrixmorgan.squark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class SquarkEngine {
    private static String TAG = "SquarkEngine";
    private static SquarkEngine sSquarkEngine;

    private int mMultiplier;
    private double mConversionRate;

    private SquarkEngine(@NonNull Context context) {
        mConversionRate = 4.2;
        mMultiplier = 1;
    }

    public static synchronized SquarkEngine newInstance(@NonNull Context context) {
        if (sSquarkEngine == null) {
            sSquarkEngine = new SquarkEngine(context);
        }
        return sSquarkEngine;
    }

    public static synchronized SquarkEngine getInstance() {
        return sSquarkEngine;
    }

    public void updateTable(ArrayList<TextView> quantifiers, ArrayList<TextView> results) {
        for (int i = 0; i < 10; i++) {
            quantifiers.get(i).setText(String.valueOf(mMultiplier * (i + 1)));
            results.get(i).setText(String.valueOf(new BigDecimal((i + 1) * mMultiplier * mConversionRate).setScale(2, BigDecimal.ROUND_HALF_UP)));
        }
    }
}

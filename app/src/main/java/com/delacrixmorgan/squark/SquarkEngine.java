package com.delacrixmorgan.squark;

import android.content.Context;
import android.icu.math.BigDecimal;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class SquarkEngine {
    private static String TAG = "SquarkEngine";
    private static SquarkEngine sSquarkEngine;

    private double mConversionRate;

    private SquarkEngine(@NonNull Context context) {
        mConversionRate = 4.2;
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

    public void updateTable(Context context, TableLayout tableLayout, int multiplier) {
//        for (int i = 1; i <= 10; i++) {
//            View subRow = LayoutInflater.from(context).inflate(R.layout.view_row, null);
//
//            ((TextView) subRow.findViewById(R.id.row_quantifier)).setText(String.valueOf(i * multiplier));
//            ((TextView) subRow.findViewById(R.id.row_result)).setText(String.valueOf(new BigDecimal(i * multiplier * mConversionRate).setScale(2, BigDecimal.ROUND_HALF_UP)));
//
//            tableLayout.addView(subRow);
//        }
    }
}

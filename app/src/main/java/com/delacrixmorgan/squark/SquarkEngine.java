package com.delacrixmorgan.squark;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.delacrixmorgan.squark.model.Currency;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class SquarkEngine {
    private static String TAG = "SquarkEngine";
    private static SquarkEngine sSquarkEngine;

    private double mMultiplier;
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

    public void updateConversionRate(Currency baseCurrency, Currency quoteCurrency) {

        mConversionRate = quoteCurrency.getRate() / baseCurrency.getRate();

        Log.i(TAG, "updateConversionRate (Base) : " + baseCurrency.getRate());
        Log.i(TAG, "updateConversionRate (Quote): " + quoteCurrency.getRate());
        Log.i(TAG, "updateConversionRate (Rate) : " + mConversionRate);
    }

    public void updateTable(Context context, ArrayList<TextView> quantifiers, ArrayList<TextView> results) {
        DecimalFormat resultFormat = new DecimalFormat("###,##0.00");
        BigDecimal bigQuantifier, bigResult;

        for (int i = 0; i < 10; i++) {
            bigQuantifier = new BigDecimal(mMultiplier * (i + 1)).setScale(2, BigDecimal.ROUND_HALF_UP);
            bigResult = new BigDecimal((i + 1) * mMultiplier * mConversionRate).setScale(2, BigDecimal.ROUND_HALF_UP);

            quantifiers.get(i).setText(String.valueOf(resultFormat.format(bigQuantifier)));
            results.get(i).setText(String.valueOf(resultFormat.format(bigResult)));

            quantifiers.get(i).startAnimation(AnimationUtils.loadAnimation(context, R.anim.wobble));
            results.get(i).startAnimation(AnimationUtils.loadAnimation(context, R.anim.wobble));
        }
    }

    public void swipeLeft() {
        if (mMultiplier < 1000000) {
            mMultiplier *= 10;
        }
    }

    public void swipeRight() {
        if (mMultiplier > 0.1) {
            mMultiplier /= 10;
        }
    }
}
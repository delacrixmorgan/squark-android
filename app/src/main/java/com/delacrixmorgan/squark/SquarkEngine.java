package com.delacrixmorgan.squark;

import android.app.Activity;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.delacrixmorgan.squark.listener.OnSwipeTouch;
import com.delacrixmorgan.squark.model.Currency;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class SquarkEngine {
    private static String TAG = "SquarkEngine";
    private static SquarkEngine sSquarkEngine;

    private double mMultiplier, mConversionRate;
    private Boolean mTableExpanded;
    private DecimalFormat mDecimalFormat;
    private BigDecimal mBigQuantifier, mBigResult;

    private SquarkEngine() {
        mConversionRate = 1;
        mMultiplier = 1;

        mTableExpanded = false;
        mDecimalFormat = new DecimalFormat("###,##0.00");
    }

    public static synchronized SquarkEngine newInstance() {
        if (sSquarkEngine == null) {
            sSquarkEngine = new SquarkEngine();
        }
        return sSquarkEngine;
    }

    public static synchronized SquarkEngine getInstance() {
        return sSquarkEngine;
    }

    public void updateConversionRate(Currency baseCurrency, Currency quoteCurrency) {
        mTableExpanded = false;
        mConversionRate = quoteCurrency.getRate() / baseCurrency.getRate();
    }

    public void updateTable(Activity activity, TableLayout tableLayout) {
        tableLayout.removeAllViews();

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = (TableRow) activity.getLayoutInflater().inflate(R.layout.view_row, tableLayout, false);
            TextView quantifierTextView = (TextView) tableRow.findViewById(R.id.view_row_quantifier);
            TextView resultTextView = (TextView) tableRow.findViewById(R.id.view_row_result);

            double m1 = mMultiplier * (i + 1);
            double m2 = m1 * mConversionRate;

            mBigQuantifier = new BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP);
            mBigResult = new BigDecimal(m2).setScale(2, BigDecimal.ROUND_HALF_UP);

            quantifierTextView.setText(String.valueOf(mDecimalFormat.format(mBigQuantifier)));
            resultTextView.setText(String.valueOf(mDecimalFormat.format(mBigResult)));

            quantifierTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble));
            resultTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble));

            setupRowListener(activity, tableLayout, quantifierTextView, i);
            setupRowListener(activity, tableLayout, resultTextView, i);

            tableLayout.addView(tableRow);
        }
    }

    private void expandTable(Activity activity, TableLayout tableLayout, int expandQuantifier) {
        tableLayout.removeAllViews();

        for (int i = 0; i < 11; i++) {
            TableRow tableRow = (TableRow) activity.getLayoutInflater().inflate(R.layout.view_row, tableLayout, false);
            TextView quantifierTextView = (TextView) tableRow.findViewById(R.id.view_row_quantifier);
            TextView resultTextView = (TextView) tableRow.findViewById(R.id.view_row_result);

            double m1 = ((expandQuantifier + 1) * mMultiplier) + ((mMultiplier / 10) * i);
            double m2 = m1 * mConversionRate;

            mBigQuantifier = new BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP);
            mBigResult = new BigDecimal(m2).setScale(2, BigDecimal.ROUND_HALF_UP);

            quantifierTextView.setText(String.valueOf(mDecimalFormat.format(mBigQuantifier)));
            resultTextView.setText(String.valueOf(mDecimalFormat.format(mBigResult)));

            quantifierTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble));
            resultTextView.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble));

            setupRowListener(activity, tableLayout, quantifierTextView, i);
            setupRowListener(activity, tableLayout, resultTextView, i);

            tableLayout.addView(tableRow);
        }
    }

    private void setupRowListener(final Activity activity, final TableLayout tableLayout, TextView quantifier, final int expandQuantifier) {
        quantifier.setOnTouchListener(new OnSwipeTouch(activity) {
            @Override
            public void onSingleTap() {
                if (mTableExpanded && (expandQuantifier == 0)) {
                    mTableExpanded = false;
                    updateTable(activity, tableLayout);

                } else {
                    if (!mTableExpanded) {
                        mTableExpanded = true;

                        expandTable(activity, tableLayout, expandQuantifier);
                        changeExpandedRow(activity, tableLayout);
                    }
                    tableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.wobble));
                }
            }

            @Override
            public void onSwipeLeft() {
                if (!mTableExpanded) {
                    mMultiplier = mMultiplier < 1000000 ? mMultiplier *= 10 : mMultiplier;
                    updateTable(activity, tableLayout);
                }
            }

            @Override
            public void onSwipeRight() {
                if (!mTableExpanded) {
                    mMultiplier = mMultiplier > 0.1 ? mMultiplier /= 10 : mMultiplier;
                    updateTable(activity, tableLayout);
                }
            }
        });
    }

    private void changeExpandedRow(Activity activity, TableLayout tableLayout) {
        TextView m1 = (TextView) tableLayout.getChildAt(0).findViewById(R.id.view_row_quantifier);
        TextView m2 = (TextView) tableLayout.getChildAt(0).findViewById(R.id.view_row_result);

        m1.setBackgroundColor(activity.getResources().getColor(R.color.amber));
        m2.setBackgroundColor(activity.getResources().getColor(R.color.amber));

        m1.setTextColor(activity.getResources().getColor(R.color.black));
        m2.setTextColor(activity.getResources().getColor(R.color.black));
    }
}
package com.delacrixmorgan.squark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.delacrixmorgan.squark.listener.OnSwipeTouch;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Delacrix Morgan on 03/10/2017.
 */

public class GuideActivity extends Activity {
    private static String TAG = "GuideActivity";
    private TableLayout mTableLayout;
    private TextView mGuideText;
    private double mMultiplier;

    private LinearLayout mSingleLayout, mExpandedLayout;
    private DecimalFormat mDecimalFormat, mPointFormat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mDecimalFormat = new DecimalFormat("###,##0");
        mPointFormat = new DecimalFormat("###,##0.00");
        mMultiplier = 1;

        mSingleLayout = (LinearLayout) findViewById(R.id.activity_guide_single_layout);
        mExpandedLayout = (LinearLayout) findViewById(R.id.activity_guide_expanded_layout);

        mGuideText = (TextView) findViewById(R.id.activity_guide_text);
        mTableLayout = (TableLayout) findViewById(R.id.activity_guide_table);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_row, mTableLayout, false);
            mTableLayout.addView(tableRow);
        }

        mGuideText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingleLayout.setVisibility(View.GONE);
                mExpandedLayout.setVisibility(View.VISIBLE);

                expandTable();
            }
        });

        findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouch(this) {
            @Override
            public void onSwipeLeft() {
                if (mMultiplier < 1000000) {
                    mMultiplier *= 10;
                }
                wobbleText();
            }

            @Override
            public void onSwipeRight() {
                if (mMultiplier > 1) {
                    mMultiplier /= 10;
                }
                wobbleText();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void expandTable() {
        mTableLayout.removeAllViews();

        for (int i = 0; i < 11; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_row_guide, mTableLayout, false);
            TextView quantifierTextView = (TextView) tableRow.findViewById(R.id.view_row_quantifier);

            double m1 = mMultiplier + ((mMultiplier / 10) * i);

            BigDecimal bigQuantifier = new BigDecimal(m1).setScale(2, BigDecimal.ROUND_HALF_UP);

            quantifierTextView.setText(String.valueOf(mMultiplier == 1 ? mPointFormat.format(bigQuantifier) : mDecimalFormat.format(bigQuantifier)));
            quantifierTextView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.wobble));

            if (i == 0) {
                quantifierTextView.setBackgroundColor(getResources().getColor(R.color.amber));
                quantifierTextView.setTextColor(getResources().getColor(R.color.black));
            }

            final int expandQuantifier = i;

            tableRow.setOnTouchListener(new OnSwipeTouch(this) {
                @Override
                public void onSingleTap() {
                    if (expandQuantifier == 0) {
                        mSingleLayout.setVisibility(View.VISIBLE);
                        mExpandedLayout.setVisibility(View.GONE);
                    } else {
                        mTableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
                    }
                }

                @Override
                public void onSwipeLeft() {
                    mTableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
                }

                @Override
                public void onSwipeRight() {
                    mTableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
                }
            });

            mTableLayout.addView(tableRow);
        }
    }

    private void wobbleText() {
        mGuideText.setText(String.valueOf(mDecimalFormat.format(new BigDecimal(mMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP))));
        mGuideText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
    }

    @Override
    public void onBackPressed() {
        if (mExpandedLayout.getVisibility() == View.VISIBLE) {
            mSingleLayout.setVisibility(View.VISIBLE);
            mExpandedLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}

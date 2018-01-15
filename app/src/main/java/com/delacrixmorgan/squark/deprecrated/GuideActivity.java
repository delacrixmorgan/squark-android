package com.delacrixmorgan.squark.deprecrated;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.delacrixmorgan.squark.R;
import com.delacrixmorgan.squark.deprecrated.listener.OnSwipeTouch;
import com.delacrixmorgan.squark.deprecrated.shared.Helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Delacrix Morgan on 03/10/2017.
 */

public class GuideActivity extends Activity {
    private static String TAG = "GuideActivity";
    private TableLayout mTableLayout;
    private Button mStartButton;
    private TextView mGuideValue, mGuideHint;
    private ImageView mGuideArrow;
    private double mMultiplier;

    private LinearLayout mSingleLayout, mExpandedLayout;
    private DecimalFormat mDecimalFormat, mPointFormat;

    private Boolean mSwipeLeft, mSwipeRight, mExpand;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mSwipeLeft = false;
        mSwipeRight = false;
        mExpand = false;

        mDecimalFormat = new DecimalFormat("###,##0");
        mPointFormat = new DecimalFormat("###,##0.00");
        mMultiplier = 1;

        mSingleLayout = (LinearLayout) findViewById(R.id.activity_guide_single_layout);
        mExpandedLayout = (LinearLayout) findViewById(R.id.activity_guide_expanded_layout);

        mGuideValue = (TextView) findViewById(R.id.activity_guide_value);
        mGuideHint = (TextView) findViewById(R.id.activity_guide_hint);
        mGuideArrow = (ImageView) findViewById(R.id.activity_guide_arrow);

        mStartButton = (Button) findViewById(R.id.activity_guide_start_button);
        mTableLayout = (TableLayout) findViewById(R.id.activity_guide_table);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_row, mTableLayout, false);
            mTableLayout.addView(tableRow);
        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(Helper.SHARED_PREFERENCE, MODE_PRIVATE).edit();
                editor.putBoolean(Helper.QUICK_GUIDE_PREFERENCE, true);
                editor.apply();

                finish();
            }
        });

        mGuideValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSingleLayout.setVisibility(View.GONE);
                mExpandedLayout.setVisibility(View.VISIBLE);
                mStartButton.setVisibility(View.GONE);

                expandTable();
            }
        });

        mGuideHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wobbleText();
            }
        });

        findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouch(this) {
            @Override
            public void onSwipeLeft() {
                mSwipeLeft = true;
                mGuideArrow.setRotation(180);
                cycleHints();

                if (mMultiplier < 1000000) {
                    mMultiplier *= 10;
                } else {
                    mGuideHint.setText("Okay, Swipe Left");
                    mGuideArrow.setRotation(0);
                    mGuideArrow.setBackgroundResource(R.drawable.ic_keyboard_backspace_white_48dp);
                }
                wobbleText();
            }

            @Override
            public void onSwipeRight() {
                mSwipeRight = true;
                mGuideArrow.setRotation(0);
                cycleHints();

                if (mMultiplier > 1) {
                    mMultiplier /= 10;
                } else {
                    mGuideHint.setText("Alright, Swipe Right");
                    mGuideArrow.setRotation(180);
                    mGuideArrow.setBackgroundResource(R.drawable.ic_keyboard_backspace_white_48dp);
                }
                wobbleText();
            }
        });
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
                        collapseExpandedLayout();
                        cycleHints();
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

                @Override
                public void onSwipeTop() {
                    mTableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
                }

                @Override
                public void onSwipeBottom() {
                    mTableLayout.getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
                }
            });

            mTableLayout.addView(tableRow);
        }
    }

    @Override
    public void onBackPressed() {
        if (mExpandedLayout.getVisibility() == View.VISIBLE) {
            collapseExpandedLayout();
        } else {
            super.onBackPressed();
        }
    }

    private void cycleHints() {
        if (!mSwipeLeft) {
            mGuideHint.setText("Beautiful, Swipe Left");
        } else if (!mSwipeRight) {
            mGuideHint.setText("Wonderful, Swipe Right");
        } else if (!mExpand) {
            mGuideHint.setText("Sweet, Click Number");
            mGuideArrow.setRotation(90);
        } else {
            mGuideHint.setText("You're All Set!");
            mGuideArrow.setRotation(0);
            mGuideArrow.setBackgroundResource(R.drawable.ic_check_white_48dp);
            mStartButton.setVisibility(View.VISIBLE);
        }
    }

    private void collapseExpandedLayout() {
        mExpand = true;
        cycleHints();
        mSingleLayout.setVisibility(View.VISIBLE);
        mExpandedLayout.setVisibility(View.GONE);
    }

    private void wobbleText() {
        mGuideValue.setText(String.valueOf(mDecimalFormat.format(new BigDecimal(mMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP))));
        mGuideValue.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
    }
}

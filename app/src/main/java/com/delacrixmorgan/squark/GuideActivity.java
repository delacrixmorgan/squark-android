package com.delacrixmorgan.squark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Delacrix Morgan on 03/10/2017.
 */

public class GuideActivity extends Activity {
    private static String TAG = "GuideActivity";
    private TextView mGuideText;
    private double mMultiplier;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        TableLayout tableLayout = (TableLayout) findViewById(R.id.activity_guide_table);

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_row, tableLayout, false);
            tableLayout.addView(tableRow);
        }

//        mGuideText = (TextView) findViewById(R.id.activity_guide_text);
//        mMultiplier = 1;
//
//        findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouch(this) {
//            @Override
//            public void onSwipeLeft() {
//                if (mMultiplier < 1000000) {
//                    mMultiplier *= 10;
//                }
//                wobbleText();
//            }
//
//            @Override
//            public void onSwipeRight() {
//                if (mMultiplier > 1) {
//                    mMultiplier /= 10;
//                }
//                wobbleText();
//            }
//        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void wobbleText() {
        DecimalFormat resultFormat = new DecimalFormat("###,##0");

        mGuideText.setText(String.valueOf(resultFormat.format(new BigDecimal(mMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP))));
        mGuideText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
        mGuideText.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.wobble));
    }
}

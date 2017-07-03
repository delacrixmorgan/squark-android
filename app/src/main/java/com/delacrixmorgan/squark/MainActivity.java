package com.delacrixmorgan.squark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_vg_fragment, new CurrencyFragment())
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

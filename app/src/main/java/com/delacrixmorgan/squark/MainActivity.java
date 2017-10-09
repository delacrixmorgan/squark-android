package com.delacrixmorgan.squark;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;

import com.delacrixmorgan.squark.fragment.TableFragment;
import com.delacrixmorgan.squark.shared.Helper;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.delacrixmorgan.squark.shared.Helper.SHARED_PREFERENCE;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class MainActivity extends Activity {
    private static String TAG = "MainActivity";
    private TableFragment mTableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTableFragment = new TableFragment();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        SquarkEngine.newInstance();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        changeAppOverview();

        if (!getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE).getBoolean(Helper.QUICK_GUIDE_PREFERENCE, false)) {
            startActivity(new Intent(MainActivity.this, GuideActivity.class));
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_vg_fragment, mTableFragment)
                .commit();
    }

    private void changeAppOverview() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.squark_logo_coin);
        setTaskDescription(new ActivityManager.TaskDescription(null, bm, color));

        bm.recycle();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (SquarkEngine.getInstance().getmTableExpanded() && getFragmentManager().getBackStackEntryCount() == 0) {
            mTableFragment.updateCurrency();
        } else {
            super.onBackPressed();
        }
    }
}

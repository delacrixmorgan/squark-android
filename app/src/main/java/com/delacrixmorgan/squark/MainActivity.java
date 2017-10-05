package com.delacrixmorgan.squark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.delacrixmorgan.squark.fragment.TableFragment;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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

        //startActivity(new Intent(this, GuideActivity.class));

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

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_vg_fragment, mTableFragment)
                .commit();
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

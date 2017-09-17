package com.delacrixmorgan.squark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.delacrixmorgan.squark.adapter.CurrencyAdapter;
import com.delacrixmorgan.squark.fragment.TableFragment;
import com.delacrixmorgan.squark.network.InterfaceAPI;
import com.delacrixmorgan.squark.network.SquarkAPI;
import com.delacrixmorgan.squark.wrapper.RestWrapper;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Delacrix Morgan on 03/07/2017.
 */

public class MainActivity extends Activity {
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SquarkEngine.newInstance(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/LeagueSpartan-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_vg_fragment, new TableFragment())
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

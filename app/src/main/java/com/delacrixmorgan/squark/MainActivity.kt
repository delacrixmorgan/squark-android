package com.delacrixmorgan.squark

import android.app.ActivityManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.delacrixmorgan.squark.common.showFragment
import com.delacrixmorgan.squark.fragment.TableFragment
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Delacrix Morgan on 03/07/2017.
 **/

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(realmConfiguration)
        SquarkEngine.newInstance()

        changeAppOverview()

        showFragment(this@MainActivity, TableFragment())
    }

    private fun changeAppOverview() {
        val typedValue = TypedValue()
        val theme = theme
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data

        val bm = BitmapFactory.decodeResource(resources, R.drawable.squark_logo_coin)
        setTaskDescription(ActivityManager.TaskDescription(null, bm, color))

        bm.recycle()
    }
}

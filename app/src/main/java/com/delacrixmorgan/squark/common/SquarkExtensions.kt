package com.delacrixmorgan.squark.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.TableLayout
import android.widget.TableRow
import com.delacrixmorgan.squark.R
import kotlinx.android.synthetic.main.view_row.view.*
import java.math.BigDecimal

/**
 * Created by Delacrix Morgan on 14/01/2018.
 **/

fun showFragment(context: Context, fragment: Fragment) {
    val activity = context as FragmentActivity

    activity.supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
}

fun changeAppOverview(activity: AppCompatActivity, theme: Resources.Theme) {
    val typedValue = TypedValue()
    val colour = typedValue.data
    val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.squark_logo_coin)

    activity.setTaskDescription(ActivityManager.TaskDescription(null, bitmap, colour))
    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
    bitmap.recycle()
}
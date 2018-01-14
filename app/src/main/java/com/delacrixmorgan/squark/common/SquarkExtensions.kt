package com.delacrixmorgan.squark.common

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.delacrixmorgan.squark.R

/**
 * Created by Delacrix Morgan on 14/01/2018.
 **/

fun showFragment(context: Context, fragment: BaseFragment) {
    val activity = context as FragmentActivity

    activity.supportFragmentManager
            .beginTransaction()
            .add(R.id.mainContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
}
package com.delacrixmorgan.squark.support

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.delacrixmorgan.squark.R

/**
 * CreditActivity
 * squark-android
 *
 * Created by Morgan Koh on 13/06/2019.
 * Copyright (c) 2019 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CreditActivity : AppCompatActivity() {
    companion object {
        fun newLaunchIntent(context: Context) = Intent(context, CreditActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit)

        val fragment = CreditListFragment.newInstance()
        this.supportFragmentManager.transaction {
            replace(R.id.contentContainer, fragment)
        }
    }
}
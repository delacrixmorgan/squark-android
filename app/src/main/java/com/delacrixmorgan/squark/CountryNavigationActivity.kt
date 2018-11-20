package com.delacrixmorgan.squark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.delacrixmorgan.squark.country.CountryListFragment
import com.delacrixmorgan.squark.support.SettingsListFragment
import com.delacrixmorgan.squark.support.SupportListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_country_navigation.*

/**
 * CountryNavigationActivity
 * squark-android
 *
 * Created by Morgan Koh on 17/11/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

class CountryNavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val EXTRA_RESULT_COUNTRY_CODE = "countryCode"

        fun newLaunchIntent(
                context: Context,
                countryCode: String? = null
        ): Intent {
            val launchIntent = Intent(context, CountryNavigationActivity::class.java)

            countryCode?.let {
                launchIntent.putExtra(EXTRA_RESULT_COUNTRY_CODE, it)
            }

            return launchIntent
        }
    }

    private var countryCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this.intent.extras != null) {
            this.countryCode = this.intent.extras.getString(EXTRA_RESULT_COUNTRY_CODE)
        }

        setContentView(R.layout.activity_country_navigation)

        this.setSupportActionBar(this.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setHomeButtonEnabled(true)
        this.title = ""

        if (savedInstanceState == null) {
            this.supportFragmentManager.beginTransaction()
                    .add(this.contentContainer.id, CountryListFragment.newInstance(this.countryCode))
                    .commit()
        }

        setupListeners()
    }

    private fun setupListeners() {
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val existingFragment = this.supportFragmentManager.findFragmentById(this.contentContainer.id)
        val targetFragment: Fragment = when (menuItem.itemId) {
            R.id.itemCountries -> {
                CountryListFragment.newInstance(this.countryCode)
            }

            R.id.itemSupport -> {
                SupportListFragment.newInstance()
            }

            R.id.itemSettings -> {
                SettingsListFragment.newInstance()
            }

            else -> {
                CountryListFragment.newInstance()
            }
        }

        if (existingFragment != null && existingFragment::class.java == targetFragment::class.java) {
            return true
        }

        this.supportFragmentManager.beginTransaction()
                .replace(this.contentContainer.id, targetFragment, targetFragment.javaClass.simpleName)
                .commitAllowingStateLoss()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
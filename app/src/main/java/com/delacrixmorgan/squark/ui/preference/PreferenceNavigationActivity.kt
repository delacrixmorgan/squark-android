package com.delacrixmorgan.squark.ui.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.databinding.ActivityPreferenceNavigationBinding
import com.delacrixmorgan.squark.ui.preference.country.CountryListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class PreferenceNavigationActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val EXTRA_RESULT_COUNTRY_CODE = "countryCode"

        fun newLaunchIntent(context: Context, countryCode: String? = null): Intent {
            val launchIntent = Intent(context, PreferenceNavigationActivity::class.java)

            countryCode?.let {
                launchIntent.putExtra(EXTRA_RESULT_COUNTRY_CODE, it)
            }

            return launchIntent
        }
    }

    private var countryCode: String? = null

    private lateinit var binding: ActivityPreferenceNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (this.intent.extras != null) {
            this.countryCode = this.intent.getStringExtra(EXTRA_RESULT_COUNTRY_CODE)
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigationView.selectedItemId = R.id.itemCountries
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val existingFragment =
            this.supportFragmentManager.findFragmentById(binding.contentContainer.id)
        val targetFragment: Fragment = when (menuItem.itemId) {
            R.id.itemCountries -> CountryListFragment.create(this.countryCode)
            R.id.itemSupport -> SupportListFragment.create()
            R.id.itemSettings -> SettingsListFragment.create()
            else -> CountryListFragment.create()
        }

        if (existingFragment != null && existingFragment::class.java == targetFragment::class.java) {
            return true
        }

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(binding.contentContainer.id, targetFragment)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
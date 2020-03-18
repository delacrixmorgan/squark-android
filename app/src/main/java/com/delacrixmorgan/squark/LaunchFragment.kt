package com.delacrixmorgan.squark

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.delacrixmorgan.squark.common.SharedPreferenceHelper.updatedTimeStamp
import com.delacrixmorgan.squark.common.isNetworkAvailable
import com.delacrixmorgan.squark.data.api.SquarkService
import com.delacrixmorgan.squark.data.api.SquarkServiceClient
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.controller.CountryDatabase
import com.delacrixmorgan.squark.data.getJsonMap
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_launch.*
import java.util.*

class LaunchFragment : Fragment() {

    private var currencies = listOf<Currency>()
    private var disposable: Disposable? = null
    private var countryDatabase: CountryDatabase? = null

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.countryDatabase = CountryDatabase.getInstance(view.context)
        CountryDataController.populateMaps(view.context)
        fetchCurrencyData()
    }

    private fun fetchCurrencyData() {
        AsyncTask.execute {
            if (requireContext().isNetworkAvailable) {
                val countryData = this.countryDatabase?.countryDataDao()?.getCountries() ?: listOf()
                if (countryData.isEmpty()) {
                    initCurrencies()
                } else {
                    launchCurrencyNavigationFragment(countryData)
                }
            } else {
                fallbackCurrencies()
            }
        }
    }

    private fun initCurrencies() {
        fallbackCurrencies()
//        this.disposable = SquarkServiceClient
//                .create(requireContext())
//                .getCurrencies()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ result ->
//                    this.currencies = result.quotes.map { Currency(code = it.key, rate = it.value) }
//                    if (this.currencies.isNotEmpty()) {
//                        insertCountries()
//                    } else {
//                        fallbackCurrencies()
//                    }
//                }, {
//                    Snackbar.make(this.mainContainer, "${it.message}", Snackbar.LENGTH_SHORT).show()
//                    fallbackCurrencies()
//                })
    }

    private fun insertCountries() {
        val database = this.countryDatabase ?: return
        val countries = arrayListOf<Country>()

        CountryDataController.countryMap.forEach { country ->
            val currency = this.currencies.firstOrNull {
                it.code == "USD${country.key}"
            }

            if (currency != null) {
                countries.add(Country(code = country.key, name = country.value, rate = currency.rate))
            }
        }

        countries.forEach {
            AsyncTask.execute {
                database.countryDataDao().insertCountry(it)
            }
        }

        this.sharedPreferences.edit { putLong(updatedTimeStamp, Date().time) }
        launchCurrencyNavigationFragment(countries)
    }

    private fun fallbackCurrencies() {
        val context = requireContext()
        val currencyMap = context.getJsonMap(R.raw.data_currency, "quotes")
        this.currencies = currencyMap.map { Currency(code = it.key, rate = it.value.toDouble()) }
        if (this.currencies.isNotEmpty()) {
            insertCountries()
        }
    }

    private fun launchCurrencyNavigationFragment(countries: List<Country>) {
        CountryDataController.updateDataSet(countries)

        val action = LaunchFragmentDirections.actionLaunchFragmentToCurrencyNavigationFragment()
        Navigation.findNavController(this.rootView).navigate(action)
    }

    override fun onPause() {
        super.onPause()
        this.disposable?.dispose()
    }

    override fun onDestroy() {
        CountryDatabase.destroyInstance()
        super.onDestroy()
    }
}
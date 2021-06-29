package com.delacrixmorgan.squark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.data.api.SquarkResult
import com.delacrixmorgan.squark.data.controller.CountryDataController
import com.delacrixmorgan.squark.data.dao.CountryDataDao
import com.delacrixmorgan.squark.data.dao.CountryDatabase
import com.delacrixmorgan.squark.data.model.Country
import com.delacrixmorgan.squark.data.model.Currency
import com.delacrixmorgan.squark.data.service.SquarkService
import com.delacrixmorgan.squark.databinding.FragmentLaunchBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class LaunchFragment : Fragment(R.layout.fragment_launch) {
    private var countryDatabaseDao: CountryDataDao? = null

    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentLaunchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            countryDatabaseDao = CountryDatabase.getInstance(requireContext())?.countryDataDao()
            val countries = countryDatabaseDao?.getCountries()
            if (!countries.isNullOrEmpty()) {
                CountryDataController.updateDataSet(countries)
                launchCurrencyNavigationFragment()
            } else {
                fetchCountries()
            }
        }
    }

    override fun onDestroy() {
        CountryDatabase.destroyInstance()
        super.onDestroy()
    }

    private fun fetchCountries() {
        lifecycleScope.launch {
            val currencies = fetchCurrencies() ?: fetchFallbackCurrencies()
            addCountryDatabase(currencies)
        }
    }

    private suspend fun fetchCurrencies(): List<Currency>? {
        return when (val result = SquarkService.getCurrencies()) {
            is SquarkResult.Success -> {
                result.value.currencies
            }
            is SquarkResult.Failure -> {
                Snackbar.make(
                    binding.mainContainer,
                    result.error.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
                null
            }
        }
    }

    private fun fetchFallbackCurrencies(): List<Currency> {
        return requireContext().getJsonMap(R.raw.data_currency, "quotes")
            .map { Currency(code = it.key, rate = it.value.toDouble()) }
    }

    private suspend fun addCountryDatabase(currencies: List<Currency>) {
        val countries = CountryDataController.countryMap.mapNotNull { country ->
            val currency = currencies.firstOrNull { it.code == "USD${country.key}" }
            Country(code = country.key, name = country.value, rate = currency?.rate ?: 0.0)
        }

        countries.forEach { countryDatabaseDao?.insertCountry(it) }
        CountryDataController.updateDataSet(countries)

        SharedPreferenceHelper.lastUpdatedDate = Date()
        launchCurrencyNavigationFragment()
    }

    private fun launchCurrencyNavigationFragment() {
        val action = LaunchFragmentDirections.actionLaunchFragmentToCurrencyNavigationFragment()
        Navigation.findNavController(binding.rootView).navigate(action)
    }
}
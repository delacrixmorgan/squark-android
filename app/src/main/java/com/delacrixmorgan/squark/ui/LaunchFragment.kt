package com.delacrixmorgan.squark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.delacrixmorgan.squark.R
import com.delacrixmorgan.squark.common.getJsonMap
import com.delacrixmorgan.squark.databinding.FragmentLaunchBinding
import com.delacrixmorgan.squark.models.LegacyCurrency
import com.delacrixmorgan.squark.services.network.Result
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LaunchFragment : Fragment(R.layout.fragment_launch) {
    private val binding get() = requireNotNull(_binding)
    private var _binding: FragmentLaunchBinding? = null

    private val viewModel: LaunchViewModel by viewModels()

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
//        CoroutineScope(Dispatchers.IO).launch {
//            countryDatabaseDao.getCountries().collect { countries ->
//                if (countries.isNotEmpty()) {
//                    CountryDataController.updateDataSet(countries)
//                    launchCurrencyNavigationFragment()
//                } else {
//                    fetchCountries()
//                }
//                coroutineContext.job.cancel()
//            }
//        }
    }

    private fun fetchCountries() {
        CoroutineScope(Dispatchers.IO).launch {
            val currencies = fetchCurrencies()
            addCountryDatabase(currencies)
        }
    }

    private suspend fun fetchCurrencies(): List<LegacyCurrency> {
        return when (val result = viewModel.fetchCurrencies()) {
            is Result.Success -> {
                result.value.currencies
            }
            is Result.Failure -> {
                Snackbar.make(
                    binding.mainContainer,
                    result.error.localizedMessage ?: "",
                    Snackbar.LENGTH_SHORT
                ).show()
                fetchFallbackCurrencies()
            }
        }
    }

    private fun fetchFallbackCurrencies(): List<LegacyCurrency> {
        return requireContext().getJsonMap(R.raw.data_currency, "quotes")
            .map { LegacyCurrency(code = it.key, rate = it.value.toDouble()) }
    }

    private fun addCountryDatabase(currencies: List<LegacyCurrency>) {
//        val countries = CountryDataController.countryMap.mapNotNull { country ->
//            val currency = currencies.firstOrNull { it.code == "USD${country.key}" }
//            Country(code = country.key, name = country.value, rate = currency?.rate ?: 0.0)
//        }
//
//        countries.forEach { countryDatabaseDao.insertCountry(it) }
//        CountryDataController.updateDataSet(countries)
//
//        SharedPreferenceHelper.lastUpdatedDate = Date()
//        launchCurrencyNavigationFragment()
    }

    private fun launchCurrencyNavigationFragment() {
        lifecycleScope.launch {
            findNavController().navigate(
                LaunchFragmentDirections.actionLaunchFragmentToCurrencyNavigationFragment()
            )
        }
    }
}
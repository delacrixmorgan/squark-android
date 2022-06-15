package com.delacrixmorgan.squark.ui.preference.country

import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.services.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    suspend fun fetchCurrencies() = currencyRepository.fetchCurrencies()
}

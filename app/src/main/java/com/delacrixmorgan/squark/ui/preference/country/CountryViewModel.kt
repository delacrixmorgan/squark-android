package com.delacrixmorgan.squark.ui.preference.country

import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.services.repository.CurrencyRepository

class CountryViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    suspend fun fetchCurrencies() = currencyRepository.fetchCurrencies()
}

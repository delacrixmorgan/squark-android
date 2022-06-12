package com.delacrixmorgan.squark.ui

import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.services.repository.CurrencyRepository

class LaunchViewModel(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    suspend fun fetchCurrencies() = currencyRepository.fetchCurrencies()
}
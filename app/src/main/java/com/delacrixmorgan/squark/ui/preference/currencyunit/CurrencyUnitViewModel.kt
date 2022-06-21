package com.delacrixmorgan.squark.ui.preference.currencyunit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import com.delacrixmorgan.squark.models.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyUnitViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CurrencyUnitUiState>(CurrencyUnitUiState.Start)
    val uiState: StateFlow<CurrencyUnitUiState> get() = _uiState

    private var selectedCountry: Currency? = null
    private var currencies = listOf<Currency>()
    private var filterJob: Job? = null

    fun updateSelectedCurrency(currency: Currency) {
        viewModelScope.launch {
            selectedCountry = currency
            fetchCurrencies()
        }
    }

    private suspend fun fetchCurrencies() {
        _uiState.value = CurrencyUnitUiState.Loading
        getCurrencyUseCase().collect { result ->
            result.fold(
                success = {
                    currencies = it
                    _uiState.value = CurrencyUnitUiState.Success(it)
                },
                failure = {
                    _uiState.value = CurrencyUnitUiState.Failure(it)
                }
            )
        }
    }

    fun refreshCurrencies(isForcedRefreshed: Boolean = false) {
        // TODO (Check if it's a Day Old)
//        val lastUpdatedDateTime = SharedPreferenceHelper.lastUpdatedDate.time
//        val currentDateTime = Date().time
//
//        if (TimeUnit.MILLISECONDS.toDays(currentDateTime - lastUpdatedDateTime) >= 1) {
////            updateCurrencyRates()
//        }
        _uiState.value = CurrencyUnitUiState.OnRefreshed(currencies, isUpdatedAlready = true)
    }

    fun filterCurrencies(query: String? = null, isSearchMode: Boolean) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            delay(300)
            val filteredCurrencies = currencies.filter { currency ->
                if (!query.isNullOrBlank()) {
                    currency.name.lowercase().contains(query) || currency.code.lowercase().contains(query)
                } else {
                    true
                }
            }
            _uiState.value = CurrencyUnitUiState.OnCurrencyFiltered(filteredCurrencies)
        }
    }
}

sealed class CurrencyUnitUiState {
    object Start : CurrencyUnitUiState()
    object Loading : CurrencyUnitUiState()
    data class Success(val currencies: List<Currency>) : CurrencyUnitUiState()
    data class OnRefreshed(val currencies: List<Currency>, val isUpdatedAlready: Boolean) : CurrencyUnitUiState()
    data class OnCurrencyFiltered(val filteredCurrencies: List<Currency>) : CurrencyUnitUiState()
    data class Failure(val exception: Exception) : CurrencyUnitUiState()
}

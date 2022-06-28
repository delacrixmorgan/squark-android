package com.delacrixmorgan.squark.ui.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacrixmorgan.squark.common.SharedPreferenceHelper
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import com.delacrixmorgan.squark.models.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CurrencyUiState>(CurrencyUiState.Start)
    val uiState: StateFlow<CurrencyUiState> get() = _uiState

    private var currencies = listOf<Currency>()
    val baseCurrency: Currency?
        get() = currencies.firstOrNull { it.code == SharedPreferenceHelper.baseCurrency }

    val quoteCurrency: Currency?
        get() = currencies.firstOrNull { it.code == SharedPreferenceHelper.quoteCurrency }

    var anchorPosition = 0F
    var multiplier: Double = 1.0
    var conversionRate: Double = 1.0

    fun onStart() {
        viewModelScope.launch {
            fetchCurrencies()
        }
    }

    private suspend fun fetchCurrencies() {
        getCurrencyUseCase().collect { result ->
            result.fold(
                success = {
                    currencies = it
                    _uiState.value = CurrencyUiState.Success(it)
                },
                failure = {
                    _uiState.value = CurrencyUiState.Failure(it)
                }
            )
        }
    }

    fun updateConversionRate(baseRate: Double? = 1.0, quoteRate: Double? = 1.0) {
        conversionRate = (quoteRate ?: 1.0) / (baseRate ?: 1.0)
    }
}

sealed class CurrencyUiState {
    object Start : CurrencyUiState()
    data class Success(val currencies: List<Currency>) : CurrencyUiState()
    data class Failure(val exception: Exception) : CurrencyUiState()
}

package com.delacrixmorgan.squark.ui.preference.currencyunit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import com.delacrixmorgan.squark.models.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyUnitViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private val _uiState = MutableSharedFlow<CurrencyUnitUiState>()
    val uiState: SharedFlow<CurrencyUnitUiState> get() = _uiState

    var selectedCurrency: Currency? = null
    private var currencies = listOf<Currency>()
    private var filterJob: Job? = null

    fun onStart() {
        viewModelScope.launch {
            fetchCurrencies()
        }
    }

    fun fetchCurrencies() {
        viewModelScope.launch {
            _uiState.emit(CurrencyUnitUiState.Loading)
            getCurrencyUseCase().collect { result ->
                result.fold(
                    success = {
                        currencies = it
                        _uiState.emit(CurrencyUnitUiState.Success(currencies))
                    },
                    failure = {
                        _uiState.emit(CurrencyUnitUiState.Failure(it))
                    }
                )
            }
        }
    }

    fun filterCurrencies(query: String? = null) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch {
            delay(300)
            val filteredCurrencies = currencies.filter { currency ->
                if (!query.isNullOrBlank()) {
                    currency.name.contains(query, ignoreCase = true) || currency.code.contains(query, ignoreCase = true)
                } else {
                    true
                }
            }
            _uiState.emit(CurrencyUnitUiState.OnCurrencyFiltered(filteredCurrencies))
        }
    }
}

sealed class CurrencyUnitUiState {
    object Loading : CurrencyUnitUiState()
    data class Success(val currencies: List<Currency>) : CurrencyUnitUiState()
    data class OnCurrencyFiltered(val filteredCurrencies: List<Currency>) : CurrencyUnitUiState()
    data class Failure(val exception: Exception) : CurrencyUnitUiState()
}

package com.delacrixmorgan.squark.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Start)
    val uiState: StateFlow<LaunchUiState> get() = _uiState

    init {
        viewModelScope.launch {
            fetchCurrencies()
        }
    }

    private suspend fun fetchCurrencies() {
        getCurrencyUseCase().collect { result ->
            result.fold(
                success = { _uiState.value = LaunchUiState.Success },
                failure = { _uiState.value = LaunchUiState.Failure(it) }
            )
        }
    }
}

sealed class LaunchUiState {
    object Start : LaunchUiState()
    object Success : LaunchUiState()
    data class Failure(val exception: Exception) : LaunchUiState()
}
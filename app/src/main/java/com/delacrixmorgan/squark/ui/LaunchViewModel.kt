package com.delacrixmorgan.squark.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import com.delacrixmorgan.squark.service.remoteconfig.FirebaseRemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase,
    private val remoteConfigManager: FirebaseRemoteConfigManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchUiState>(LaunchUiState.Start)
    val uiState: StateFlow<LaunchUiState> get() = _uiState

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { fetchCurrencies() }
            withContext(Dispatchers.IO) { updateRemoteConfigValues() }
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

    private suspend fun updateRemoteConfigValues(): Unit = suspendCoroutine { continuation ->
        remoteConfigManager.fetchAndActivate(object : FirebaseRemoteConfigManager.FetchRemoteConfigDataListener {
            override fun onSuccess() {
                continuation.resume(Unit)
            }

            override fun onFailure(e: Exception?) {
                continuation.resume(Unit)
            }
        })
    }
}

sealed class LaunchUiState {
    object Start : LaunchUiState()
    object Success : LaunchUiState()
    data class Failure(val exception: Exception) : LaunchUiState()
}
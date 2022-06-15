package com.delacrixmorgan.squark.ui

import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.data.usecase.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {
    suspend fun fetchCurrencies() {
        getCurrencyUseCase().collect { result ->
            result.fold(
                success = {

                },
                failure = {

                }
            )
        }
    }
}
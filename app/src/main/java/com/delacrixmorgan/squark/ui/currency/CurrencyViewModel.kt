package com.delacrixmorgan.squark.ui.currency

import androidx.lifecycle.ViewModel
import com.delacrixmorgan.squark.models.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor() : ViewModel() {
    var baseCurrency: Currency? = null
    var quoteCurrency: Currency? = null

    var anchorPosition = 0F
    var multiplier: Double = 1.0
    var conversionRate: Double = 1.0

    fun updateConversionRate(baseRate: Double? = 1.0, quoteRate: Double? = 1.0) {
        conversionRate = (quoteRate ?: 1.0) / (baseRate ?: 1.0)
    }
}
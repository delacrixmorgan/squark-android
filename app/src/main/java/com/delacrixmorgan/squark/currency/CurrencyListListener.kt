package com.delacrixmorgan.squark.currency

import com.delacrixmorgan.squark.data.model.Currency

/**
 * CurrencyListListener
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

interface CurrencyListListener {
    fun onCurrencyClick(currency: Currency)
}
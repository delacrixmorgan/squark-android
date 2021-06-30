package com.delacrixmorgan.squark.models

class CurrencyResult(
    private val quotes: Map<String, Double> = mapOf()
) {
    val currencies: List<Currency>
        get() = quotes.map { Currency(code = it.key, rate = it.value) }
}
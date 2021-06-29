package com.delacrixmorgan.squark.data.model

class CurrencyModel(
    val quotes: Map<String, Double> = mapOf()
) {
//    private val terms: String? = null
//    private val success: Boolean? = null
//    private val privacy: String? = null
//    private val source: String? = null
//    private val timestamp: Int? = null
//    private val quotes: Map<String, Double> = mapOf()

    val currencies: List<Currency>
        get() = quotes.map { Currency(code = it.key, rate = it.value) }
}
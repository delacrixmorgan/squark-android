package com.delacrixmorgan.squark.models

data class CurrencyQuotes(
    private val quotes: Map<String, Double> = mapOf()
)
package com.delacrixmorgan.squark.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

object CurrencyModel {
    data class Result(
        @SerializedName("terms")
        val terms: String? = null,

        @SerializedName("success")
        val success: Boolean? = null,

        @SerializedName("privacy")
        val privacy: String? = null,

        @SerializedName("source")
        val source: String? = null,

        @SerializedName("timestamp")
        val timestamp: Int? = null,

        @SerializedName("quotes")
        @Expose
        val quotes: Map<String, Double>
    ) {
        val currencies: List<Currency>
            get() = quotes.map { Currency(code = it.key, rate = it.value) }
    }
}
package com.delacrixmorgan.squark.data.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

object CountryModel {
    data class Result(
        @SerializedName("terms")
        val terms: String? = null,

        @SerializedName("success")
        val success: Boolean? = null,

        @SerializedName("privacy")
        val privacy: String? = null,

        @SerializedName("currencies")
        @Expose
        val quotes: Map<String, String>? = null
    )
}
package com.delacrixmorgan.squark.data.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * CountryModel
 * squark-android
 *
 * Created by Delacrix Morgan on 14/06/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

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
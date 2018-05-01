package com.delacrixmorgan.squark.data.model

/**
 * Currency
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class Currency(
        val code: String,
        val country: String,
        val description: String,
        val rate: Double)
package com.delacrixmorgan.squark.data.api

import java.sql.Timestamp

/**
 * SquarkResult
 * squark-android
 *
 * Created by Delacrix Morgan on 15/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

data class SquarkResult(
        val success: Boolean,
        val terms: String,
        val privacy: String,
        val timestamp: Timestamp,
        val source: String,
        val quotes: HashMap<String, Double>
)
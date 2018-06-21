package com.delacrixmorgan.squark.data.model

import android.arch.persistence.room.ColumnInfo

data class Currency(
        @ColumnInfo(name = "currencyCode") val code: String,
        @ColumnInfo(name = "rate") var rate: Double
)
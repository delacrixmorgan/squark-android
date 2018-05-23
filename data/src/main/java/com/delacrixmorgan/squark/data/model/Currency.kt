package com.delacrixmorgan.squark.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Currency
 * squark-android
 *
 * Created by Delacrix Morgan on 01/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Entity(tableName = "Currency")
data class Currency(
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name = "code") val code: String,
        @ColumnInfo(name = "country") val country: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "rate") var rate: Double
)
package com.delacrixmorgan.squark.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Country
 * squark-android
 *
 * Created by Delacrix Morgan on 14/06/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Entity(tableName = "Country")
data class Country(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "code") val code: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "rate") var rate: Double
)
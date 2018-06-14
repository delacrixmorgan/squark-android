package com.delacrixmorgan.squark.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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
        @ColumnInfo(name = "name") val code: String
)
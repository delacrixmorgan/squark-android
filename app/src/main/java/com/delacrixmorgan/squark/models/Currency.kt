package com.delacrixmorgan.squark.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Currency")
data class Currency(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rate") var rate: Double
)

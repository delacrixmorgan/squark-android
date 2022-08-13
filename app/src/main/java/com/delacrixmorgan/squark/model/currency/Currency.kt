package com.delacrixmorgan.squark.model.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Entity(tableName = "Currency")
@Serializable
data class Currency(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "rate") var rate: Double
) {
    override fun toString(): String {
        return Json.encodeToString(serializer(), this)
    }
}

fun String.toCurrency(): Currency {
    return Json.decodeFromString(Currency.serializer(), this)
}
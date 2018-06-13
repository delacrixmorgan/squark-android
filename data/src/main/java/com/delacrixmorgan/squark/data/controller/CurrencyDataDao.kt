package com.delacrixmorgan.squark.data.controller

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.delacrixmorgan.squark.data.model.Currency

/**
 * CurrencyDataDao
 * squark-android
 *
 * Created by Delacrix Morgan on 23/05/2018.
 * Copyright (c) 2018 licensed under a Creative Commons Attribution-ShareAlike 4.0 International License.
 */

@Dao
interface CurrencyDataDao {
    @Query("SELECT * from Currency")
    fun getCurrencies(): List<Currency>

    @Insert(onConflict = REPLACE)
    fun insertCurrency(currency: Currency)

    @Query("DELETE from Currency")
    fun deleteCurrencies()
}